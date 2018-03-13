package is.yranac.canary.fragments.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.databinding.FragmentSetupGetHelpBinding;
import is.yranac.canary.fragments.ModeTutorialFragment;
import is.yranac.canary.messages.ModalPopped;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.Utils;
import is.yranac.canary.util.ZendeskUtil;

import static is.yranac.canary.model.device.DeviceType.CANARY_AIO;

/**
 * Created by Schroeder on 6/27/16.
 */
public class GetHelpFragment extends SetUpBaseFragment {


    protected static String LOG_TAG = "GetHelpFragment";
    private FragmentSetupGetHelpBinding binding;
    private GetHelpType getHelpType;

    public enum GetHelpType {
        GET_HELP_TYPE_AUDIO,
        GET_HELP_TYPE_CANARY_ONE_SETUP,
        GET_HELP_TYPE_BLUETOOTH,
        GET_HELP_TYPE_INTERNET,
        GET_HELP_TYPE_OTA,
        GET_HELP_MASKING,
        GET_HELP_TYPE_MOTION,
        GET_HELP_TYPE_MEMBERSHIP,
        GET_HELP_TYPE_NO_MEMBERSHIP,
        GET_HELP_TYPE_MEMBER,
        GET_HELP_TYPE_CHANGE_WIFI,
        GET_HELP_TYPE_GEOFENCE,
        GET_HELP_TYPE_FAILED_OTA_CANARY,
        GET_HELP_TYPE_MODES,
        GET_HELP_TYPE_HOME_MODES,
        GET_HELP_TYPE_NIGHT_MODES,
        GET_HELP_TYPE_BATTERY,
        GET_HELP_TYPE_FAILED_OTA_FLEX,
        GET_HELP_TYPE_FAILED_OTA_VIEW,
        GET_HELP_TYPE_PRESENCE_NOTIFICATIONS
    }

    public class MyHandlers {
        public void linkOne(View view) {
            String url;
            switch (getHelpType) {
                case GET_HELP_TYPE_AUDIO:
                    url = getString(R.string.canary_cannot_hear_help_help_link);
                    break;
                case GET_HELP_TYPE_BLUETOOTH:
                case GET_HELP_TYPE_CHANGE_WIFI:
                case GET_HELP_TYPE_CANARY_ONE_SETUP:
                    url = getString(R.string.bluetooth_help_link);
                    break;
                case GET_HELP_TYPE_INTERNET:
                    url = getString(R.string.wifi_password_help_link);
                    break;
                case GET_HELP_TYPE_OTA:
                    url = getString(R.string.setup_length_help_link);
                    break;
                case GET_HELP_MASKING:
                    url = getString(R.string.what_are_masks_url);
                    break;
                case GET_HELP_TYPE_MOTION:
                    url = getString(R.string.help_center_site_motion);
                    break;
                case GET_HELP_TYPE_MEMBER:
                    url = getString(R.string.what_is_a_member_url);
                    break;
                case GET_HELP_TYPE_MEMBERSHIP:
                    url = getString(R.string.what_comes_with_membership_url);
                    break;
                case GET_HELP_TYPE_GEOFENCE:
                    url = getString(R.string.how_does_canary_know_im_away_url);
                    break;
                case GET_HELP_TYPE_NO_MEMBERSHIP:
                    url = getString(R.string.what_is_canary_membership_url);
                    break;
                case GET_HELP_TYPE_FAILED_OTA_CANARY:
                    url = getString(R.string.setup_failed_canary);
                    break;
                case GET_HELP_TYPE_FAILED_OTA_FLEX:
                    url = getString(R.string.setup_failed_flex);
                    break;
                case GET_HELP_TYPE_FAILED_OTA_VIEW:
                    url = getString(R.string.setup_failed_view);
                    break;
                case GET_HELP_TYPE_MODES:
                case GET_HELP_TYPE_HOME_MODES:
                case GET_HELP_TYPE_NIGHT_MODES: {
                    getActivity().onBackPressed();
                    ModeTutorialFragment fragment = ModeTutorialFragment.newInstance(ModeTutorialFragment.TutorialType.MODE_TUTORIAL);
                    fragment.setDeviceType(new DeviceType(CANARY_AIO));
                    addFragmentToStack(fragment, Utils.NO_PUSH_ANIMATION);
                    return;
                }
                case GET_HELP_TYPE_BATTERY:
                    url = getString(R.string.adjust_recording_range_url);
                    break;
                case GET_HELP_TYPE_PRESENCE_NOTIFICATIONS:
                    url = getString(R.string.how_do_presence_notifications_work_url);
                    break;
                default:
                    return;
            }

            loadUrl(url);
        }

        public void linkTwo(View view) {
            String url;
            switch (getHelpType) {
                case GET_HELP_TYPE_AUDIO:
                    url = getString(R.string.volume_increase_help_link);
                    break;
                case GET_HELP_TYPE_BLUETOOTH:
                    url = getString(R.string.device_activated_help_link);
                    break;
                case GET_HELP_TYPE_CANARY_ONE_SETUP:
                    url = getString(R.string.bluetooth_help_link);
                    break;
                case GET_HELP_TYPE_INTERNET:
                    url = getString(R.string.wifi_or_ethernet_help_link);
                    break;
                case GET_HELP_MASKING:
                    url = getString(R.string.most_out_of_masks_url);
                    break;
                case GET_HELP_TYPE_MOTION:
                    url = getString(R.string.help_center_motion_notifications);
                    break;
                case GET_HELP_TYPE_MEMBER:
                    url = getString(R.string.how_can_i_add_members_url);
                    break;
                case GET_HELP_TYPE_MEMBERSHIP:
                    url = getString(R.string.what_is_home_deducible_url);
                    break;
                case GET_HELP_TYPE_GEOFENCE:
                    url = getString(R.string.why_wont_canary_know_home_or_away_url);
                    break;
                case GET_HELP_TYPE_NO_MEMBERSHIP:
                    url = getString(R.string.what_is_a_location_url);
                    break;
                case GET_HELP_TYPE_MODES:
                case GET_HELP_TYPE_HOME_MODES:
                case GET_HELP_TYPE_NIGHT_MODES:
                    url = getString(R.string.what_are_modes_url);
                    break;

                case GET_HELP_TYPE_BATTERY:
                    url = getString(R.string.impove_battery_life_url);
                    break;
                case GET_HELP_TYPE_PRESENCE_NOTIFICATIONS:
                    url = getString(R.string.help_center_motion_notifications);
                    break;
                default:
                    return;
            }

            loadUrl(url);
        }

        public void linkThree(View view) {
            String url;
            switch (getHelpType) {
                case GET_HELP_TYPE_CANARY_ONE_SETUP: {
                    getActivity().onBackPressed();
                    Bundle bundle = new Bundle();
                    bundle.putInt(device_type, CANARY_AIO);
                    bundle.putBoolean(key_isSetup, true);
                    bundle.putString(key_location_uri, locationUri());
                    SetCanaryConnectionTypeFragment fragment = SetCanaryConnectionTypeFragment.newInstance(bundle);
                    addFragmentToStack(fragment, Utils.NO_PUSH_ANIMATION);
                    return;
                }
                case GET_HELP_TYPE_AUDIO:
                    url = getString(R.string.device_activated_help_link);
                    break;
                case GET_HELP_MASKING:
                    url = getString(R.string.masks_notifications_url);
                    break;
                case GET_HELP_TYPE_MEMBERSHIP:
                    url = getString(R.string.how_do_i_update_my_payment_method_url);
                    break;
                case GET_HELP_TYPE_GEOFENCE:
                    url = getString(R.string.how_can_i_trouble_shoot_location_settings_url);
                    break;
                case GET_HELP_TYPE_NO_MEMBERSHIP:
                    url = getString(R.string.how_can_i_remove_a_location_url);
                    break;
                case GET_HELP_TYPE_MODES:
                    url = getString(R.string.what_is_night_mode_url);
                    break;
                case GET_HELP_TYPE_HOME_MODES:
                    url = getString(R.string.why_does_it_say_private_url);
                    break;
                case GET_HELP_TYPE_NIGHT_MODES:
                    url = getString(R.string.what_is_night_mode_url);
                    break;
                case GET_HELP_TYPE_PRESENCE_NOTIFICATIONS:
                    url = getString(R.string.how_does_canary_know_if_home_url);
                    break;
                default:
                    return;
            }

            loadUrl(url);
        }

        public void linkFour(View view) {
            String url;
            switch (getHelpType) {
                case GET_HELP_TYPE_AUDIO:
                    url = getString(R.string.wifi_password_help_link);
                    break;
                case GET_HELP_TYPE_MEMBERSHIP:
                    url = getString(R.string.what_is_a_location_url);
                    break;
                case GET_HELP_TYPE_MODES:
                    url = getString(R.string.why_does_it_say_private_url);
                    break;
                case GET_HELP_TYPE_HOME_MODES:
                    url = getString(R.string.how_does_canary_know_if_home_url);
                    break;
                case GET_HELP_TYPE_NIGHT_MODES:
                    url = getString(R.string.why_does_it_say_private_url);
                    break;
                case GET_HELP_TYPE_PRESENCE_NOTIFICATIONS:
                    url = getString(R.string.why_wont_canary_know_home_or_away_url);
                    break;
                default:
                    return;
            }

            loadUrl(url);
        }

        public void linkFive(View view) {
            String url;
            switch (getHelpType) {
                case GET_HELP_TYPE_MODES:
                    url = getString(R.string.how_does_canary_know_if_home_url);
                    break;
                case GET_HELP_TYPE_HOME_MODES:
                    url = getString(R.string.why_dont_i_receive_notifications_url);
                    break;
                case GET_HELP_TYPE_NIGHT_MODES:
                    url = getString(R.string.why_dont_i_receive_notifications_url);
                    break;
                case GET_HELP_TYPE_PRESENCE_NOTIFICATIONS:
                    url = getString(R.string.how_can_i_trouble_shoot_location_settings_url);
                    break;
                default:
                    return;
            }

            loadUrl(url);
        }

        public void linkSix(View view) {
            String url;
            switch (getHelpType) {
                case GET_HELP_TYPE_MODES:
                    url = getString(R.string.why_dont_i_receive_notifications_url);
                    break;
                default:
                    return;
            }

            loadUrl(url);
        }


        public void linkHelpCenter(View view) {
            loadUrl(Constants.CANARY_HELP());
        }

        public void linkZendesk(View view) {
            ZendeskUtil.showZendesk(getActivity(), 0);

        }


    }


    private void loadUrl(String url) {
        ZendeskUtil.loadHelpCenter(getContext(), url);
    }

    public static GetHelpFragment newInstance(GetHelpType getHelpType) {
        GetHelpFragment fragment = new GetHelpFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", getHelpType);
        fragment.setArguments(bundle);
        return fragment;
    }

    public class GetHelpStringTypes {
        public String buttonOneString;
        public String buttonTwoString;
        public String buttonThreeString;
        public String buttonFourString;
        public String buttonFiveString;
        public String buttonSixString;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetupGetHelpBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.header.headerViewLeftButton.setVisibility(View.GONE);
        binding.header.headerViewRightButton.setVisibility(View.GONE);
        getHelpType = (GetHelpType) getArguments().getSerializable("type");
        GetHelpStringTypes getHelpStringTypes = new GetHelpStringTypes();

        if (getHelpType == GetHelpType.GET_HELP_MASKING) {
            fragmentStack.showHeader(false);
        }


        switch (getHelpType) {
            case GET_HELP_TYPE_BLUETOOTH:
                getHelpStringTypes.buttonOneString = getString(R.string.why_cant_I_pair);
                getHelpStringTypes.buttonTwoString = getString(R.string.why_is_my_device_activate);
                break;
            case GET_HELP_TYPE_OTA:
                getHelpStringTypes.buttonOneString = getString(R.string.how_long_should_this_take);
                break;
            case GET_HELP_TYPE_CANARY_ONE_SETUP:
                getHelpStringTypes.buttonOneString = getString(R.string.why_cant_I_pair);
                getHelpStringTypes.buttonTwoString = getString(R.string.why_is_my_device_activate);
                getHelpStringTypes.buttonThreeString = getString(R.string.activate_with_yelow_cable);
                break;
            case GET_HELP_TYPE_AUDIO:
                getHelpStringTypes.buttonOneString = getString(R.string.cannot_hear_canary);
                getHelpStringTypes.buttonTwoString = getString(R.string.i_cant_increase_volume);
                getHelpStringTypes.buttonThreeString = getString(R.string.why_is_my_device_activate);
                getHelpStringTypes.buttonFourString = getString(R.string.why_wont_canary_accept_wifi);
                break;

            case GET_HELP_TYPE_INTERNET:
                getHelpStringTypes.buttonOneString = getString(R.string.why_wont_canary_accept_wifi);
                getHelpStringTypes.buttonTwoString = getString(R.string.should_use_ethernet_wifi);
                break;
            case GET_HELP_MASKING:
                getHelpStringTypes.buttonOneString = getString(R.string.what_are_masks);
                getHelpStringTypes.buttonTwoString = getString(R.string.most_out_of_masks);
                getHelpStringTypes.buttonThreeString = getString(R.string.masks_notifications);
                break;
            case GET_HELP_TYPE_MOTION:
                getHelpStringTypes.buttonOneString = getString(R.string.how_does_motion_sensitivity_work);
                getHelpStringTypes.buttonTwoString = getString(R.string.why_notifications_from_canary);
                break;
            case GET_HELP_TYPE_MEMBERSHIP:
                getHelpStringTypes.buttonOneString = getString(R.string.what_comes_with_membership);
                getHelpStringTypes.buttonTwoString = getString(R.string.how_do_i_update_my_payment);
                getHelpStringTypes.buttonThreeString = getString(R.string.what_is_a_location);
                break;
            case GET_HELP_TYPE_MEMBER:
                getHelpStringTypes.buttonOneString = getString(R.string.what_is_a_member);
                getHelpStringTypes.buttonTwoString = getString(R.string.how_can_i_add_or_remove_members);
                break;
            case GET_HELP_TYPE_GEOFENCE:
                getHelpStringTypes.buttonOneString = getString(R.string.how_does_canary_knwow_when_home);
                getHelpStringTypes.buttonTwoString = getString(R.string.why_dont_canary_detect_home);
                getHelpStringTypes.buttonThreeString = getString(R.string.how_can_i_troubleshoot_locaition_settings);
                break;
            case GET_HELP_TYPE_CHANGE_WIFI:
                getHelpStringTypes.buttonOneString = getString(R.string.why_cant_I_pair);
                break;
            case GET_HELP_TYPE_NO_MEMBERSHIP:
                getHelpStringTypes.buttonOneString = getString(R.string.what_is_canary_membership);
                getHelpStringTypes.buttonTwoString = getString(R.string.what_is_a_location);
                getHelpStringTypes.buttonThreeString = getString(R.string.how_can_i_remove_a_location);
                break;
            case GET_HELP_TYPE_FAILED_OTA_CANARY:
            case GET_HELP_TYPE_FAILED_OTA_FLEX:
            case GET_HELP_TYPE_FAILED_OTA_VIEW:
                getHelpStringTypes.buttonOneString = getString(R.string.why_did_the_update_fail);
                break;
            case GET_HELP_TYPE_MODES:
                getHelpStringTypes.buttonOneString = getString(R.string.how_mode_works);
                getHelpStringTypes.buttonTwoString = getString(R.string.what_are_modes);
                getHelpStringTypes.buttonThreeString = getString(R.string.what_is_night_modes);
                getHelpStringTypes.buttonFourString = getString(R.string.why_does_it_say_watch_live_is_off);
                getHelpStringTypes.buttonFiveString = getString(R.string.how_does_canary_know_home_awy);
                getHelpStringTypes.buttonSixString = getString(R.string.why_dont_i_receive_notifications);
                break;
            case GET_HELP_TYPE_HOME_MODES:
                getHelpStringTypes.buttonOneString = getString(R.string.how_mode_works);
                getHelpStringTypes.buttonTwoString = getString(R.string.what_are_modes);
                getHelpStringTypes.buttonThreeString = getString(R.string.why_does_it_say_watch_live_is_off);
                getHelpStringTypes.buttonFourString = getString(R.string.how_does_canary_know_home_awy);
                getHelpStringTypes.buttonFiveString = getString(R.string.why_dont_i_receive_notifications);
                break;
            case GET_HELP_TYPE_NIGHT_MODES:
                getHelpStringTypes.buttonOneString = getString(R.string.how_mode_works);
                getHelpStringTypes.buttonTwoString = getString(R.string.what_are_modes);
                getHelpStringTypes.buttonThreeString = getString(R.string.what_is_night_modes);
                getHelpStringTypes.buttonFourString = getString(R.string.why_does_it_say_watch_live_is_off);
                getHelpStringTypes.buttonFiveString = getString(R.string.why_dont_i_receive_notifications);
                break;
            case GET_HELP_TYPE_BATTERY:
                getHelpStringTypes.buttonOneString = getString(R.string.how_do_i_adjust_recording_range);
                getHelpStringTypes.buttonTwoString = getString(R.string.how_do_i_improve_battery_life);
                break;
            case GET_HELP_TYPE_PRESENCE_NOTIFICATIONS:
                getHelpStringTypes.buttonOneString = getString(R.string.how_do_presence_notifications_work);
                getHelpStringTypes.buttonTwoString = getString(R.string.why_dont_i_receive_notifcations);
                getHelpStringTypes.buttonThreeString = getString(R.string.why_doesnt_canary_know_if_home_away);
                getHelpStringTypes.buttonFourString = getString(R.string.why_wont_canary_know_if_home_away);
                getHelpStringTypes.buttonFiveString = getString(R.string.how_canary_i_troubleshoot_location_settings);
                break;
            default:
                return;
        }

        binding.setStringTypes(getHelpStringTypes);
        binding.setHandlers(new MyHandlers());

    }

    @Override
    public void onResume() {
        super.onResume();
        binding.header.headerTitleTextView.setText(R.string.help);
        binding.subHeaderTextView.setText(getString(R.string.how_can_we_help));
        binding.visetHelpCenterText.setText(getString(R.string.visi_help_center));
        binding.contactSupportText.setText(getString(R.string.contact_customer_support));
        fragmentStack.enableBackButton();
    }

    @Override
    public void onRightButtonClick() {

    }


    @Override
    protected String getAnalyticsTag() {
        return null;
    }


    @Override
    public void onDestroy() {

        TinyMessageBus.post(new ModalPopped());
        super.onDestroy();
    }
}
