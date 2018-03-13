package is.yranac.canary.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.ui.views.CanaryTextWatcher;
import is.yranac.canary.ui.views.EditTextWithLabel;
import is.yranac.canary.util.FragmentUtils;
import is.yranac.canary.util.Utils;

abstract public class StackFragment extends BaseFragment {


    protected static final String location_id = "location_id";
    protected static final String key_deviceJSON = "device_json";
    protected static final float ZOOM_FACTOR = 15.5f;

    protected class FragmentStackNull implements FragmentStack {

        @Override
        public void addFragmentAndResetStack(StackFragment fragment, int animation) {
        }

        @Override
        public void showHeader(boolean show) {
        }

        @Override
        public void setHeaderTitle(String title) {
        }

        @Override
        public void setHeaderTitle(int id) {
        }

        @Override
        public void setButtonsHeight(int id) {

        }

        @Override
        public void setButtonsWidth(int id) {

        }

        @Override
        public void showLogoutButton(boolean show) {
        }

        @Override
        public void showRightButton(int buttonDrawable) {
        }

        @Override
        public void enableRightButton(Fragment fragment, boolean enable) {
        }

        @Override
        public void popBackToWifi() {
        }

        @Override
        public void disableBackButton() {
        }

        @Override
        public void enableBackButton() {
        }

        @Override
        public void resetSetup(String location) {
        }

        @Override
        public void rightButtonTextColor(int white) {
        }

        @Override
        public void rightButtonBackground(int white) {

        }

        @Override
        public void rightButtonBackgroundColor(int white) {
        }

        @Override
        public void popBackToWifiAndHelpMessage() {

        }

        @Override
        public void resetButtonStyle() {

        }

        @Override
        public void popBackStack(Class<? extends Fragment> fragmentClass) {
        }

        @Override
        public void showHelpButton() {

        }
    }

    public interface FragmentStack {

        void addFragmentAndResetStack(StackFragment fragment, int animation);

        void showHeader(boolean show);

        void setHeaderTitle(String title);

        void setHeaderTitle(int id);

        void setButtonsHeight(int height);

        void setButtonsWidth(int width);

        void showLogoutButton(boolean show);

        void showRightButton(int buttonDrawable);

        void enableRightButton(Fragment fragment, boolean enable);

        void popBackToWifi();

        void disableBackButton();

        void enableBackButton();

        void resetSetup(String location);

        void rightButtonTextColor(int white);

        void rightButtonBackground(int white);

        void rightButtonBackgroundColor(int white);

        void popBackToWifiAndHelpMessage();

        void resetButtonStyle();

        void popBackStack(Class<? extends Fragment> fragmentClass);

        void showHelpButton();

    }

    protected FragmentStack fragmentStack;

    public void addFragmentToStack(Fragment fragment, int animation) {
        if (fragment == null)
            return;

        FragmentActivity activity = getActivity();
        if (activity == null)
            return;

        fragmentStack.enableBackButton();

        String tag = fragment.getClass().getSimpleName();
        Utils.addFragmentToStack(activity.getSupportFragmentManager(), fragment, tag, animation);

    }

    public void addModalFragment(Fragment fragment) {
        FragmentActivity activity = getActivity();
        if (activity == null)
            return;

        String fragmentTag = fragment.getClass().getSimpleName();
        fragmentStack.enableBackButton();
        Utils.addModalFragmentToStack(activity.getSupportFragmentManager(), fragment, fragmentTag);
    }

    public void addSecondModalFragment(Fragment fragment) {
        FragmentActivity activity = getActivity();
        if (activity == null)
            return;

        fragmentStack.enableBackButton();
        String fragmentTag = fragment.getClass().getSimpleName();

        Utils.addSecondModalFragmentToStack(activity.getSupportFragmentManager(), fragment, fragmentTag);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FragmentStack) {
            fragmentStack = (FragmentStack) activity;
        } else {
            fragmentStack = new FragmentStackNull();
        }
    }

    /**
     * Function that is called when the right button is click in the header
     */
    public abstract void onRightButtonClick();

    /**
     * Interface with callbacks for the
     */
    public interface StackFragmentCallback {
        /**
         * @param allValid
         */
        void onAllEditTextHasChanged(boolean allValid);
    }

    public void setupViewWatcher(final EditTextWithLabel editText, int validation, final List<EditTextWithLabel> editTextList,
                                 final StackFragmentCallback stackFragmentCallback) {

        editText.setValidation(validation);
        editText.getEditText().addTextChangedListener(
                new CanaryTextWatcher(validation) {
                    @Override
                    public void afterTextChanged(Editable s) {
                        super.afterTextChanged(s);
                        boolean areAllValid = textIsGood(editTextList);

                        if (stackFragmentCallback != null)
                            stackFragmentCallback.onAllEditTextHasChanged(areAllValid);
                    }
                }
        );
    }


    public void onClickFocusListener(final View clickableView, final EditText focusEditText) {
        clickableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focusEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(focusEditText, InputMethodManager.SHOW_FORCED);
            }
        });
    }

    protected void showErrorStates(List<EditTextWithLabel> editTextList) {
        for (EditTextWithLabel editTextWithLabel : editTextList) {
            if (!CanaryTextWatcher.validateString(editTextWithLabel)) {
                editTextWithLabel.showErrorState();
            }
        }

    }

    protected void removeErrorStates(List<EditTextWithLabel> editTextList) {
        for (EditTextWithLabel editTextWithLabel : editTextList) {

            editTextWithLabel.resetColors();
        }

    }


    protected boolean textIsGood(List<EditTextWithLabel> editTextList) {
        for (EditTextWithLabel editTextCheck : editTextList) {
            // the editText tag is set by the validation in CanaryTextWatcher
            if (!CanaryTextWatcher.validateString(editTextCheck))
                return false;
        }
        return true;
    }

    /**
     * Setup a passwordClickListener that is usable by all extending classes
     */
    public class PasswordOnClickListener implements View.OnClickListener {
        SwitchCompat passwordCheckBox;
        EditText passwordEditText;
        Typeface typeface;

        public PasswordOnClickListener(SwitchCompat passwordCheckBox, EditText passwordEditText) {
            this.passwordCheckBox = passwordCheckBox;
            this.passwordEditText = passwordEditText;
            this.typeface = passwordEditText.getTypeface();

        }

        @Override
        public void onClick(View v) {
            passwordCheckBox.toggle();
            syncTextView();

        }

        public void syncTextView() {
            if (passwordCheckBox.isChecked()) {
                passwordEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            } else {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            passwordEditText.setTypeface(typeface);
            passwordEditText.setSelection(
                    passwordEditText.getText()
                            .length());
        }
    }

    /**
     * Hides the soft keyboard if it present
     */
    public void hideSoftKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    getActivity().getCurrentFocus()
                            .getWindowToken(), 0);
        } catch (Exception ignore) {

        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (FragmentUtils.sDisableFragmentAnimations) {
            return AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    public <T extends StackFragment> T getInstance(Class<T> clazz) {

        try {
            T fragment = clazz.newInstance();
            Bundle args = new Bundle();
            if (getArguments() != null) {
                args.putAll(getArguments());
            }
            fragment.setArguments(args);
            return fragment;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

    }

}
