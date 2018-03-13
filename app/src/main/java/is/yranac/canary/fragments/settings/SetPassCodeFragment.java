package is.yranac.canary.fragments.settings;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import is.yranac.canary.R;
import is.yranac.canary.fragments.setup.EditUserDetailsFragment;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.util.ga.AnalyticsConstants.SCREEN_PASSCODE;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_PASSCODE_UPDATE;
import static is.yranac.canary.util.ga.AnalyticsConstants.SETTINGS_TYPE_APP;

/**
 * Created by Schroeder on 2/17/15.
 */
public class SetPassCodeFragment extends SettingsFragment implements TextWatcher {

    private static final String LOG_TAG = "SetPassCodeFragment";

    public static final int CREATE = 1;
    public static final int CHANGE = 2;
    public static final int REMOVE = 3;


    private int type;


    private View passCodeCharOne;
    private View passCodeCharTwo;
    private View passCodeCharThree;
    private View passCodeCharFour;
    private EditText passCodeEditText;
    private TextView createPassCodeTextView;
    private String passCode = null;

    private View passCodeLayout;

    public static SetPassCodeFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);

        SetPassCodeFragment setPassCodeFragment = new SetPassCodeFragment();
        setPassCodeFragment.setArguments(args);

        return setPassCodeFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_passcode, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        passCodeCharOne = view.findViewById(R.id.passcode_char_one);
        passCodeCharTwo = view.findViewById(R.id.passcode_char_two);
        passCodeCharThree = view.findViewById(R.id.passcode_char_three);
        passCodeCharFour = view.findViewById(R.id.passcode_char_four);
        passCodeEditText = (EditText) view.findViewById(R.id.passcode_edit_text);
        passCodeEditText.addTextChangedListener(this);
        createPassCodeTextView = (TextView) view.findViewById(R.id.create_passcode_text_view);
        passCodeLayout = view.findViewById(R.id.passcode_layout);


        Bundle args = getArguments();


        type = args.getInt("type", CREATE);
        switch (type) {
            case CREATE:
                break;
            case CHANGE:
            case REMOVE:
                createPassCodeTextView.setText(R.string.enter_old_passcode);
                break;
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        fragmentStack.showRightButton(0);
        fragmentStack.showHeader(false);
        passCodeEditText.requestFocus();

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(passCodeEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    protected String getAnalyticsTag() {
        return SCREEN_PASSCODE;
    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.i(LOG_TAG, "before : " + s.toString());

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        switch (s.length()) {
            case 4:
                passCodeCharFour.setVisibility(View.VISIBLE);
            case 3:
                passCodeCharThree.setVisibility(View.VISIBLE);
            case 2:
                passCodeCharTwo.setVisibility(View.VISIBLE);
            case 1:
                passCodeCharOne.setVisibility(View.VISIBLE);
                break;
            default:
                passCodeCharFour.setVisibility(View.VISIBLE);
                passCodeCharThree.setVisibility(View.VISIBLE);
                passCodeCharTwo.setVisibility(View.VISIBLE);
                passCodeCharOne.setVisibility(View.VISIBLE);
                break;

        }

        if (count <= 0) {
            switch (s.length()) {
                case 0:
                    passCodeCharOne.setVisibility(View.INVISIBLE);
                case 1:
                    passCodeCharTwo.setVisibility(View.INVISIBLE);
                case 2:
                    passCodeCharThree.setVisibility(View.INVISIBLE);
                case 3:
                    passCodeCharFour.setVisibility(View.INVISIBLE);
                    break;
            }
        }


    }

    private Handler handler = new Handler();

    @Override
    public void afterTextChanged(final Editable s) {

        if (s.length() == 4) {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isVisible())
                        return;
                    if (type == REMOVE) {
                        String enteredPassCode = s.toString();
                        String savedPassCode = PreferencesUtils.getPassCode();

                        if (enteredPassCode.equals(savedPassCode)) {
                            PreferencesUtils.removePassCode();

                            Customer customer = CurrentCustomer.getCurrentCustomer();
                            GoogleAnalyticsHelper.trackSettingsEvent(SETTINGS_PASSCODE_UPDATE, SETTINGS_TYPE_APP, customer.id,
                                    null, 0, String.valueOf(true), String.valueOf(false));
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            for (int i = fm.getBackStackEntryCount() - 1; i >= 0; i--) {

                                Fragment fragment = fm.getFragments().get(i);
                                Log.i(LOG_TAG, fragment.getClass().getSimpleName());

                                fm.popBackStack();
                                if (fragment instanceof EditUserDetailsFragment)
                                    break;
                            }
                            hideSoftKeyboard();
                        } else {
                            shakeAndClear();

                        }

                    } else if (type == CHANGE) {
                        String enteredPassCode = s.toString();
                        String savedPassCode = PreferencesUtils.getPassCode();

                        if (enteredPassCode.equals(savedPassCode)) {
                            type = CREATE;
                            passCode = null;
                            createPassCodeTextView.setText(R.string.enter_new_passcode);
                            passCodeEditText.setText("");
                        } else {
                            shakeAndClear();
                        }
                    } else if (type == CREATE) {
                        if (passCode == null) {
                            passCode = s.toString().substring(0, 4);
                            passCodeEditText.setText("");
                            createPassCodeTextView.setText(R.string.confirm_passcode);


                        } else if (passCode.equalsIgnoreCase(s.toString())) {
                            PreferencesUtils.setPassCode(passCode);
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            for (int i = fm.getBackStackEntryCount() - 1; i >= 0; i--) {
                                Fragment fragment = fm.getFragments().get(i);
                                Log.i(LOG_TAG, fragment.getClass().getSimpleName());

                                fm.popBackStack();
                                if (fragment instanceof EditUserDetailsFragment)
                                    break;
                            }

                            Customer customer = CurrentCustomer.getCurrentCustomer();
                            GoogleAnalyticsHelper.trackSettingsEvent(SETTINGS_PASSCODE_UPDATE, SETTINGS_TYPE_APP, customer.id,
                                    null, 0, String.valueOf(false), String.valueOf(true));
                            hideSoftKeyboard();

                        } else {
                            shakeAndClear();
                        }
                    }
                }
            }, 500);


        }

    }

    private void shakeAndClear() {
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                passCodeEditText.setText("");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        passCodeLayout.startAnimation(shake);
    }


}
