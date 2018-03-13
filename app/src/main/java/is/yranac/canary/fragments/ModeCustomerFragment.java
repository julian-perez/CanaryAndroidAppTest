package is.yranac.canary.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.CanaryApplication;
import is.yranac.canary.R;
import is.yranac.canary.messages.AvatarUpdated;
import is.yranac.canary.messages.CustomerFragmentFinished;
import is.yranac.canary.messages.LocationCurrentModeChanging;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.customer.Customer;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.mode.Mode;
import is.yranac.canary.model.mode.ModeCache;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.services.api.LocationAPIService;
import is.yranac.canary.services.database.CustomerDatabaseService;
import is.yranac.canary.services.database.LocationDatabaseService;
import is.yranac.canary.services.database.ModeDatabaseService;
import is.yranac.canary.services.intent.APIIntentServiceSettingsInfo;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.ui.SettingsFragmentStackActivity;
import is.yranac.canary.ui.views.DraggableHeightLayout;
import is.yranac.canary.util.AlertUtils;
import is.yranac.canary.util.DensityUtil;
import is.yranac.canary.util.ImageUtils;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.StringUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.TouchTimeUtil;
import is.yranac.canary.util.TutorialUtil;
import is.yranac.canary.util.UserUtils;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.cache.location.GetLocation;
import is.yranac.canary.util.cache.location.GotLocationData;
import is.yranac.canary.util.cache.location.SyncLocationMode;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static is.yranac.canary.ui.SettingsFragmentStackActivity.night_mode_settings;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_MODE_CHANGE;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MODE;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_MODE_MANUAL;

public class ModeCustomerFragment extends Fragment {
    private static final String LOG_TAG = "ModeCustomerFragment";

    private DraggableHeightLayout customerView;
    private View triangleView;
    private ImageView avatarImageView;
    private TextView customerInitialsTextView;

    private Button callBtn;
    private Button textBtn;
    private Button emailBtn;

    private Button modeArmedButton;
    private ImageView modeArmedIcon;
    private TextView modeArmedText;
    private TextView modeArmedHeader;

    private Button modeDisarmedButton;
    private ImageView modeDisarmedIcon;
    private TextView modeDisarmedText;
    private TextView modeDisarmedHeader;

    private Button modePrivacyButton;
    private ImageView modePrivacyIcon;
    private TextView modePrivacyText;
    private TextView modePrivacyHeader;

    private ImageView homeIcon;

    private boolean triangleViewHeightChanging = false;
    private float initialY = -1f;
    private int xOffset;
    private int translateXOffset;

    private View customerLayout;
    private Button editProfileBtn;
    private View modeSelectLayout;

    private Customer customer;
    private Location location;

    private View customerActionButtonView;
    private View currentUserActionButtonView;
    private SwitchCompat privacySwitch;
    private View modeLayout;
    private Subscription subscription;


    public static ModeCustomerFragment newInstance(int customerId, float arrowX) {
        ModeCustomerFragment f = new ModeCustomerFragment();

        Bundle args = new Bundle();
        args.putInt("customerId", customerId);
        args.putFloat("arrowX", arrowX);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customer, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        customer = CustomerDatabaseService.getCustomerFromId(getArguments().getInt("customerId"));

        customerView = (DraggableHeightLayout) view.findViewById(R.id.draggable_content_view);
        customerLayout = view.findViewById(R.id.customer_info);
        modeSelectLayout = view.findViewById(R.id.mode_container);
        modeLayout = view.findViewById(R.id.mode_info_group);
        avatarImageView = (ImageView) view.findViewById(R.id.avatar_image_view);
        customerInitialsTextView = (TextView) view.findViewById(R.id.customer_initials);

        callBtn = (Button) view.findViewById(R.id.customer_call_btn);
        textBtn = (Button) view.findViewById(R.id.customer_text_btn);
        emailBtn = (Button) view.findViewById(R.id.customer_email);

        modeArmedButton = (Button) view.findViewById(R.id.state_away_btn);
        modeArmedText = (TextView) view.findViewById(R.id.armed_dsc_text_vew);
        modeArmedHeader = (TextView) view.findViewById(R.id.armed_text_view);
        modeArmedIcon = (ImageView) view.findViewById(R.id.away_mode_icon);
        modeArmedButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int iAction = event.getAction();

                if (location.isPrivate) {
                    if (iAction == MotionEvent.ACTION_DOWN) {
                        aimzatePrivacyToggle();
                    }
                    return true;
                }

                if (iAction == MotionEvent.ACTION_DOWN) {
                    modeArmedText.setTextColor(getResources().getColor(R.color.black));
                    modeArmedHeader.setTextColor(getResources().getColor(R.color.black));
                    modeArmedIcon.setSelected(true);
                } else if (iAction == MotionEvent.ACTION_UP || iAction == MotionEvent.ACTION_CANCEL) {
                    setModeButtons(location.currentMode.name);
                }
                return false;
            }
        });

        homeIcon = (ImageView) view.findViewById(R.id.home_flag_large);

        modeDisarmedButton = (Button) view.findViewById(R.id.state_home_btn);
        modeDisarmedText = (TextView) view.findViewById(R.id.disarmed_dsc_text_vew);
        modeDisarmedHeader = (TextView) view.findViewById(R.id.disarmed_text_view);
        modeDisarmedIcon = (ImageView) view.findViewById(R.id.home_mode_icon);
        modeDisarmedButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int iAction = event.getAction();

                if (location.isPrivate) {
                    if (iAction == MotionEvent.ACTION_DOWN) {
                        aimzatePrivacyToggle();
                    }
                    return true;
                }

                if (iAction == MotionEvent.ACTION_DOWN) {
                    modeDisarmedText.setTextColor(getResources().getColor(R.color.black));
                    modeDisarmedHeader.setTextColor(getResources().getColor(R.color.black));
                } else if (iAction == MotionEvent.ACTION_UP || iAction == MotionEvent.ACTION_CANCEL) {
                    setModeButtons(location.currentMode.name);
                }
                return false;
            }
        });


        modePrivacyButton = (Button) view.findViewById(R.id.state_night_btn);
        modePrivacyText = (TextView) view.findViewById(R.id.privacy_dsc_text_view);
        modePrivacyHeader = (TextView) view.findViewById(R.id.privacy_text_view);
        modePrivacyIcon = (ImageView) view.findViewById(R.id.night_mode_icon);
        modePrivacyButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                int iAction = event.getAction();

                if (location.isPrivate) {
                    if (iAction == MotionEvent.ACTION_DOWN) {
                        aimzatePrivacyToggle();
                    }
                    return true;
                }


                if (iAction == MotionEvent.ACTION_DOWN) {
                    modePrivacyText.setTextColor(getResources().getColor(R.color.black));
                    modePrivacyHeader.setTextColor(getResources().getColor(R.color.black));
                    modePrivacyIcon.setSelected(true);
                } else if (iAction == MotionEvent.ACTION_UP || iAction == MotionEvent.ACTION_CANCEL) {
                    setModeButtons(location.currentMode.name);
                }
                return false;
            }
        });

        Button presenceNotificationBtn = view.findViewById(R.id.presence_notifications);
        presenceNotificationBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SettingsFragmentStackActivity.class);
                i.setAction(SettingsFragmentStackActivity.presence_notifications);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.scale_alpha_push);
            }
        });

        modeArmedButton.setOnClickListener(stateListener);
        modeDisarmedButton.setOnClickListener(stateListener);
        modePrivacyButton.setOnClickListener(stateListener);

        triangleView = view.findViewById(R.id.triangle_view);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager()
                .getDefaultDisplay()
                .getMetrics(metrics);
        xOffset = (int) ((6 * metrics.density) + 0.5);
        translateXOffset = (metrics.widthPixels / 2) - xOffset;

        if (customerView != null) {
            customerView.setOnTouchListener(touchListener);
            customerView.setTouchSlop(
                    ViewConfiguration.get(customerView.getContext())
                            .getScaledTouchSlop());
        }
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                modeLayout.getLayoutParams().height = view.getHeight();
                modeLayout.requestLayout();
            }
        });

        privacySwitch = (SwitchCompat) view.findViewById(R.id.privacy_mode_toggle);

        location = UserUtils.getLastViewedLocation();
        if (location != null) {
            privacySwitch.setChecked(location.isPrivate);
        }

        privacySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                fadeOutAndBail();

                final String newMode;
                if (isChecked) {
                    newMode = ModeCache.privacy;
                } else {
                    newMode = location.currentMode.name;
                }

                TinyMessageBus.post(new LocationCurrentModeChanging(true, newMode));
                LocationAPIService.changePrivacyMode(location.id, isChecked, new Callback<Void>() {
                    @Override
                    public void success(Void aVoid, Response response) {
                        TinyMessageBus.post(new SyncLocationMode(location.id, ModeCache.privacy, isChecked));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        TinyMessageBus.post(new LocationCurrentModeChanging(false, newMode));

                    }
                });
            }
        });

        editProfileBtn = (Button) view.findViewById(R.id.edit_profile_btn);

        currentUserActionButtonView = view.findViewById(R.id.current_user_btn_actions);
        customerActionButtonView = view.findViewById(R.id.customer_btn_actions);

        Button modeInfo = (Button) view.findViewById(R.id.mode_settings_button);
        modeInfo.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Utils.isDemo()) {
                            AlertUtils.showGenericAlert(getActivity(), getString(R.string.mode_settings_title), getString(R.string.mode_settings_dsc));
                            return;
                        }
                        BaseActivity baseActivity = (BaseActivity) getActivity();
                        if (!baseActivity.hasInternetConnection())
                            return;
                        Intent i = new Intent(getActivity(), SettingsFragmentStackActivity.class);
                        i.putExtra("mode_settings", "mode_settings");
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.scale_alpha_push);
                    }
                }
        );

        translateTriangleViewX(getArguments().getFloat("arrowX"));

    }

    private void aimzatePrivacyToggle() {
        Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.short_shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        privacySwitch.startAnimation(shake);

    }

    @Override
    public void onResume() {
        super.onResume();

        location = UserUtils.getLastViewedLocation();

        if (customer != null)
            setCustomer(customer.id);
        else
            setCustomer(-1);


        if (location != null) {
            TinyMessageBus.post(new GetLocation(location.id));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        TinyMessageBus
                .register(this);
    }

    @Override
    public void onStop() {
        TinyMessageBus
                .unregister(this);
        super.onStop();
    }


    private void animateTriangleViewHeight(final View triangleView, final float scaleFactor) {
        if (triangleViewHeightChanging) {
            return;
        }

        triangleViewHeightChanging = true;

        triangleView.setPivotY(0f);

        ObjectAnimator oa = ObjectAnimator
                .ofFloat(triangleView, "scaleY", 1 - scaleFactor, scaleFactor)
                .setDuration(100);
        oa.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                triangleViewHeightChanging = false;
                triangleView.setVisibility(scaleFactor == 0 ? View.INVISIBLE : View.VISIBLE);

            }
        });
        oa.start();
    }

    public void showTriangleView() {
        triangleView.setVisibility(View.VISIBLE);
    }

    public void hideTriangleView() {
        triangleView.setVisibility(View.INVISIBLE);
    }

    public void setTriangleViewX(float xPosition) {
        triangleView.setX(xPosition - xOffset);
    }

    public void translateTriangleViewX(float endX) {
        if (triangleView.getX() > 0f) {
            triangleView.setTranslationX(triangleView.getX() - translateXOffset);
            ObjectAnimator
                    .ofFloat(
                            triangleView, "translationX", endX - translateXOffset)
                    .start();
        } else {
            triangleView.setTranslationX(endX - translateXOffset);
        }
    }

    private OnClickListener stateListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // mis-clicking prevention, using threshold of 250 ms
            if (TouchTimeUtil.dontAllowTouch()) {
                return;
            }
            switch (v.getId()) {
                case R.id.state_away_btn:
                    changeMode(ModeCache.away);
                    break;

                case R.id.state_home_btn:
                    changeMode(ModeCache.home);
                    break;

                case R.id.state_night_btn:
                    changeMode(ModeCache.night);

                    break;
            }
        }
    };

    private void changeMode(final String mode) {
        if (Utils.isDemo()) {
            AlertUtils.showGenericAlert(getActivity(), getString(R.string.change_modes), getString(R.string.change_modes_dsc));
            return;
        }


        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null && !activity.hasInternetConnection())
            return;
        // bail if the mode is the same as current

        Mode currentMode = location.currentMode;

        if (currentMode.name.equals(mode)) {
            return;
        }

        fadeOutAndBail();
        GoogleAnalyticsHelper.trackEvent(CATEGORY_MODE, ACTION_MODE_CHANGE, PROPERTY_MODE_MANUAL, null, location.id, 0);


        if (getActivity() != null && mode.equalsIgnoreCase(ModeCache.night)) {
            if (!TutorialUtil.isTutorialInProgress() && !PreferencesUtils.hasSeenNightModeOnboarding()) {

                final BaseActivity baseActivity = (BaseActivity) getActivity();

                APIIntentServiceSettingsInfo.updateSettingsForLocation(getContext(), UserUtils.getLastViewedLocationId());
                AlertUtils.showGenericAlert(getActivity(), getString(R.string.schudule_night_mode),
                        getString(R.string.schudule_night_mode_dsc), R.drawable.mode_night_on, getString(R.string.okay),
                        getString(R.string.set_schedule), 0, 0, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeMode(mode);
                            }
                        }, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!baseActivity.hasInternetConnection())
                                    return;

                                Intent i = new Intent(baseActivity, SettingsFragmentStackActivity.class);
                                i.setAction(night_mode_settings);
                                baseActivity.startActivity(i);
                                baseActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.scale_alpha_push);
                            }
                        });
                return;
            }
        }


        TinyMessageBus.post(new LocationCurrentModeChanging(true, mode));
        LocationAPIService.changeLocationCurrentMode(
                location.resourceUri, ModeCache.getMode(mode).resourceUri, new Callback<Void>() {
                    @Override
                    public void success(Void aVoid, Response response) {
                        TinyMessageBus.post(new SyncLocationMode(location.id, mode, false));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        TinyMessageBus.post(new LocationCurrentModeChanging(false, mode));
                        try {
                            String errorMessage = Utils.getErrorMessageFromRetrofit(getActivity(), error);

                            if (!StringUtils.isNullOrEmpty(errorMessage)) {
                                Toast.makeText(CanaryApplication.getContext(), errorMessage, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } catch (JSONException ignored) {
                        }
                    }
                });
    }

    public void showCustomerInfo() {
        customerLayout.setVisibility(View.VISIBLE);
        modeSelectLayout.setVisibility(View.GONE);

        Customer currentCustomer = CurrentCustomer.getCurrentCustomer();

        if (currentCustomer.id == customer.id) {
            customerActionButtonView.setVisibility(View.GONE);
            currentUserActionButtonView.setVisibility(View.VISIBLE);

            editProfileBtn.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (Utils.isDemo()) {
                                if (TouchTimeUtil.dontAllowTouch())
                                    return;
                                AlertUtils.showGenericAlert(getActivity(), getString(R.string.my_profile_title), getString(R.string.my_profile_dsc));
                                return;
                            }

                            BaseActivity baseActivity = (BaseActivity) getActivity();
                            if (!baseActivity.hasInternetConnection())
                                return;

                            Intent i = new Intent(getActivity(), SettingsFragmentStackActivity.class);
                            i.setAction("edit_profile");
                            startActivity(i);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.scale_alpha_push);
                        }
                    });

            View view = getView();
            if (view == null)
                return;
            ((TextView) view.findViewById(R.id.customer_name_text_view)).setText(getString(R.string.you));

        } else {
            customerActionButtonView.setVisibility(View.VISIBLE);
            currentUserActionButtonView.setVisibility(View.GONE);
            View view = getView();
            if (view == null)
                return;

            ((TextView) view.findViewById(R.id.customer_name_text_view)).setText(customer.getFullName());

            setUpCallClickListener(customer.phone);
            setUpTextClickListener(customer.phone);
            setUpEmailClickListener(customer.email);
        }

        String where;
        Location arrivedLocation = LocationDatabaseService.getLocationFromResourceUri(customer.currentLocation);
        if (arrivedLocation == null) {
            arrivedLocation = UserUtils.getLastViewedLocation();
        }

        boolean isHome = false;
        String name = "";
        if (arrivedLocation != null) {
            name = arrivedLocation.name;
            isHome = getString(R.string.home).equalsIgnoreCase(name);
        }

        Log.i(LOG_TAG, customer.currentLocation);
        Log.i(LOG_TAG, arrivedLocation.resourceUri);
        if (arrivedLocation == null || !arrivedLocation.resourceUri.equalsIgnoreCase(customer.currentLocation)) {
            if (CurrentCustomer.getCurrentCustomer().resourceUri.equalsIgnoreCase(customer.resourceUri)) {

                if (isHome) {

                    where = getString(R.string.are_not_at, name);
                } else {
                    where = getString(R.string.are_not_at_home, name);

                }
            } else {
                if (!isHome) {
                    where = getString(R.string.is_not_at, name);
                } else {
                    where = getString(R.string.is_not_at_home, name);

                }
            }
        } else {

            if (currentCustomer.id == customer.id) {
                if (!isHome) {
                    where = getString(R.string.are_at, name);
                } else {
                    where = getString(R.string.are_at_home, name);

                }
            } else {
                if (!isHome) {
                    where = getString(R.string.is_at, name);
                } else {
                    where = getString(R.string.is_at_home, name);

                }
                Log.i(LOG_TAG, where);

            }
        }

        Log.i(LOG_TAG, "Where string " + where);

        // Appends a string to the location saying for how long the user has been at that location
        View view = getView();
        if (view == null)
            return;

        ((TextView) view.findViewById(R.id.customer_sub_text_view)).setText(where);

        setUpAvatar();

    }

    private void setUpCallClickListener(final String phone) {
        callBtn.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (Utils.isDemo()) {
                            if (TouchTimeUtil.dontAllowTouch())
                                return;
                            AlertUtils.showGenericAlert(getActivity(), getString(R.string.contact_members), getString(R.string.contact_members_dsc));
                            return;
                        }
                        Intent intent = new Intent(
                                Intent.ACTION_DIAL, Uri.fromParts(
                                "tel", phone, null));
                        startActivity(intent);
                    }
                });
    }

    private void setUpTextClickListener(final String phone) {
        textBtn.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Utils.isDemo()) {
                            if (TouchTimeUtil.dontAllowTouch())
                                return;
                            AlertUtils.showGenericAlert(getActivity(), getString(R.string.contact_members), getString(R.string.contact_members_dsc));
                            return;
                        }
                        Intent intent = new Intent(
                                Intent.ACTION_VIEW, Uri.fromParts(
                                "sms", phone, null));
                        startActivity(intent);

                    }
                });
    }

    private void setUpEmailClickListener(final String email) {
        emailBtn.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (Utils.isDemo()) {
                            if (TouchTimeUtil.dontAllowTouch())
                                return;
                            AlertUtils.showGenericAlert(getActivity(), getString(R.string.contact_members), getString(R.string.contact_members_dsc));
                            return;
                        }

                        Log.i(LOG_TAG, email);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                        startActivity(Intent.createChooser(intent, "Send Email"));
                    }
                });
    }

    public void setCustomer(int customerId) {
        if (customerId == -1) {
            modeSelectLayout.setVisibility(View.VISIBLE);
            customerLayout.setVisibility(View.GONE);
            Mode currentMode = ModeDatabaseService.currentLocationModeCheck();
            setModeButtons(currentMode.name);
            customer = new Customer();
            customer.id = -1;
        } else {
            customer = CustomerDatabaseService.getCustomerFromId(customerId);
            if (customer == null)
                fadeOutAndBail();
            else
                showCustomerInfo();
        }
    }

    public void setUpAvatar() {
        customerInitialsTextView.setText(customer.getInitials());
        ImageUtils.loadAvatar(String.valueOf(customer.id), avatarImageView);
        int locationId = UserUtils.getLastViewedLocationId();

        if (customer.getCurrentLocation() == locationId) {
            homeIcon.setVisibility(View.VISIBLE);
        } else {
            homeIcon.setVisibility(View.GONE);
        }
    }


    private void setModeButtons(String mode) {
        boolean modeArmed = false;
        boolean modeDisarmed = false;
        boolean modePrivacy = false;

        if (!location.isPrivate) {
            switch (mode) {
                case ModeCache.away:
                    modeArmed = true;
                    break;
                case ModeCache.home:
                    modeDisarmed = true;
                    break;
                case ModeCache.night:
                    modePrivacy = true;
                    break;
            }
        }

        if (getActivity() == null)
            return;

        Resources resources = getActivity().getResources();
        if (modeArmed) {
            modeArmedText.setTextColor(resources.getColor(R.color.black));
            modeArmedHeader.setTextColor(resources.getColor(R.color.black));
        } else {
            modeArmedText.setTextColor(resources.getColor(R.color.gray));
            modeArmedHeader.setTextColor(resources.getColor(R.color.gray));
        }

        if (modeDisarmed) {
            modeDisarmedText.setTextColor(resources.getColor(R.color.black));
            modeDisarmedHeader.setTextColor(resources.getColor(R.color.black));
        } else {
            modeDisarmedText.setTextColor(resources.getColor(R.color.gray));
            modeDisarmedHeader.setTextColor(resources.getColor(R.color.gray));
        }

        if (modePrivacy) {
            modePrivacyText.setTextColor(resources.getColor(R.color.black));
            modePrivacyHeader.setTextColor(resources.getColor(R.color.black));
        } else {
            modePrivacyText.setTextColor(resources.getColor(R.color.gray));
            modePrivacyHeader.setTextColor(resources.getColor(R.color.gray));
        }

        modeArmedIcon.setSelected(modeArmed);
        modeDisarmedIcon.setSelected(modeDisarmed);
        modePrivacyIcon.setSelected(modePrivacy);
    }

    public int getCustomerId() {
        if (customer != null)
            return customer.id;
        return 0;
    }


    private OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent ev) {
            final int action = MotionEventCompat.getActionMasked(ev);

            final float currentY = ev.getY();

            // handle the case of the touch gesture being complete.
            if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                // Release the drag
                customerView.setIsDragging(false);

                int height = (int) (DensityUtil.dip2px(getActivity(), 14) + (initialY - currentY));

                if (height > DensityUtil.dip2px(getActivity(), 35)) {
                    animateUpAndBail();
                    return true;

                } else {
                    LayoutParams params = triangleView.getLayoutParams();

                    params.height = DensityUtil.dip2px(getActivity(), 14);
                    triangleView.setLayoutParams(params);
                    triangleView.setVisibility(View.VISIBLE);
                    modeLayout.setY(0);
                }

                initialY = -1f;
                return false; // Do not intercept touch event, let the child handle it
            }

            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    // Start the drag
                    customerView.setIsDragging(true);
                    initialY = currentY;
                }
                case MotionEvent.ACTION_MOVE:
                    if (initialY == -1f) {
                        initialY = ev.getY();
                    }

                    LayoutParams params = triangleView.getLayoutParams();
                    int height = Math.max(0, (int) (DensityUtil.dip2px(getActivity(), 14) + (initialY - currentY)));
                    float newY = currentY - initialY;
                    modeLayout.setY(newY < 0 ? newY : 0);

                    params.height = height;
                    triangleView.setLayoutParams(params);

                    if (height > DensityUtil.dip2px(getActivity(), 35)) {
                        if (triangleView.getVisibility() == View.VISIBLE) {
                            animateTriangleViewHeight(triangleView, 0);
                        }
                    } else {
                        if (triangleView.getVisibility() == View.INVISIBLE) {
                            animateTriangleViewHeight(triangleView, 1);
                        }
                    }

                    if (height > DensityUtil.dip2px(getActivity(), 80)) {
                        triangleView.setVisibility(View.INVISIBLE);
                        animateUpAndBail();
                        return true;
                    }
                    break;
            }

            return true;
        }
    };

    public void animateUpAndBail() {

        if (getParentFragment() instanceof MainLayoutFragment) {
            MainLayoutFragment mainLayoutFragment = (MainLayoutFragment) getParentFragment();
            mainLayoutFragment.fadeBlackOverlayOut();
        }

        FragmentManager fragmentManager = getParentFragment().getChildFragmentManager();
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(0, R.anim.slide_out_top)
                .remove(ModeCustomerFragment.this)
                .commitAllowingStateLoss();
        TinyMessageBus.postDelayed(new CustomerFragmentFinished(), 400);
    }

    public void fadeOutAndBail() {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity == null || baseActivity.isPaused())
            return;

        if (getParentFragment() instanceof MainLayoutFragment) {
            MainLayoutFragment mainLayoutFragment = (MainLayoutFragment) getParentFragment();
            mainLayoutFragment.fadeBlackOverlayOut();
        }


        getParentFragment().getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(0, R.anim.fade_out)
                .remove(ModeCustomerFragment.this)
                .commit();

        TinyMessageBus.post(new CustomerFragmentFinished());
    }

    @Subscribe
    public void onAvatarUpdated(AvatarUpdated message) {
        if (customer.id == message.customer.id) {
            customer = message.customer;
            setUpAvatar();
        }
    }

    @Subscribe
    public void gotLocationData(GotLocationData gotLocationData) {
        if (location == null)
            return;

        if (gotLocationData.location.id == location.id) {
            subscription = gotLocationData.subscription;
        }
    }

}
