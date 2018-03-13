
package android.databinding;
import is.yranac.canary.BR;
class DataBinderMapper  {
    final static int TARGET_MIN_SDK = 19;
    public DataBinderMapper() {
    }
    public android.databinding.ViewDataBinding getDataBinder(android.databinding.DataBindingComponent bindingComponent, android.view.View view, int layoutId) {
        switch(layoutId) {
                case is.yranac.canary.R.layout.fragment_settings_home_mode:
                    return is.yranac.canary.databinding.FragmentSettingsHomeModeBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_device_naming:
                    return is.yranac.canary.databinding.FragmentSettingsDeviceNamingBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_add_a_location:
                    return is.yranac.canary.databinding.FragmentAddALocationBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.footer_remove_location:
                    return is.yranac.canary.databinding.FooterRemoveLocationBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_location_overview:
                    return is.yranac.canary.databinding.FragmentSettingsLocationOverviewBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.activity_masking:
                    return is.yranac.canary.databinding.ActivityMaskingBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_health_graph:
                    return is.yranac.canary.databinding.FragmentHealthGraphBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_simple_list:
                    return is.yranac.canary.databinding.FragmentSettingsSimpleListBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_main_settings:
                    return is.yranac.canary.databinding.FragmentMainSettingsBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.activity_entry_detail:
                    return is.yranac.canary.databinding.ActivityEntryDetailBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.new_layout_video_playback:
                    return is.yranac.canary.databinding.NewLayoutVideoPlaybackBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_connect_cable:
                    return is.yranac.canary.databinding.FragmentSetupConnectCableBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.lte_level_view:
                    return is.yranac.canary.databinding.LteLevelViewBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_splash_screen:
                    return is.yranac.canary.databinding.FragmentSetupSplashScreenBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.alert_dialog_permission:
                    return is.yranac.canary.databinding.AlertDialogPermissionBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.avatar_layout:
                    return is.yranac.canary.databinding.AvatarLayoutBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.layout_about_device_header:
                    return is.yranac.canary.databinding.LayoutAboutDeviceHeaderBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_entrydetail_tag:
                    return is.yranac.canary.databinding.FragmentEntrydetailTagBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_get_help:
                    return is.yranac.canary.databinding.FragmentSettingsGetHelpBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_geofence_position:
                    return is.yranac.canary.databinding.FragmentSettingsGeofencePositionBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_membership:
                    return is.yranac.canary.databinding.FragmentSettingsMembershipBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_presence_notifications:
                    return is.yranac.canary.databinding.FragmentSettingsPresenceNotificationsBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_flex_connectivity:
                    return is.yranac.canary.databinding.FragmentSettingsFlexConnectivityBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_power_battery_notif:
                    return is.yranac.canary.databinding.FragmentPowerBatteryNotifBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_tutorial:
                    return is.yranac.canary.databinding.FragmentTutorialBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.layout_header:
                    return is.yranac.canary.databinding.LayoutHeaderBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.layout_device_statatics:
                    return is.yranac.canary.databinding.LayoutDeviceStataticsBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.view_battery_indicator:
                    return is.yranac.canary.databinding.ViewBatteryIndicatorBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_location_services_debugging:
                    return is.yranac.canary.databinding.FragmentLocationServicesDebuggingBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.alert_dialog_add_person:
                    return is.yranac.canary.databinding.AlertDialogAddPersonBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.change_password_fragment:
                    return is.yranac.canary.databinding.ChangePasswordFragmentBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_tutorial_masking_start:
                    return is.yranac.canary.databinding.FragmentTutorialMaskingStartBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_tutorial_setup_masks:
                    return is.yranac.canary.databinding.FragmentTutorialSetupMasksBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.listrow_send_feedback:
                    return is.yranac.canary.databinding.ListrowSendFeedbackBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.layout_homehealth:
                    return is.yranac.canary.databinding.LayoutHomehealthBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_motion_notificaions_settings:
                    return is.yranac.canary.databinding.FragmentMotionNotificaionsSettingsBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_start_membership:
                    return is.yranac.canary.databinding.FragmentSetupStartMembershipBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.activity_setup_fragment_stack:
                    return is.yranac.canary.databinding.ActivitySetupFragmentStackBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.select_device_location_header:
                    return is.yranac.canary.databinding.SelectDeviceLocationHeaderBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.edit_text_with_label:
                    return is.yranac.canary.databinding.EditTextWithLabelBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.layout_membership_benefits:
                    return is.yranac.canary.databinding.LayoutMembershipBenefitsBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_mode:
                    return is.yranac.canary.databinding.FragmentSettingsModeBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_geofence:
                    return is.yranac.canary.databinding.FragmentSettingsGeofenceBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.alert_dialog_terms_and_conditions:
                    return is.yranac.canary.databinding.AlertDialogTermsAndConditionsBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.alert_dialog_emergency_numbers:
                    return is.yranac.canary.databinding.AlertDialogEmergencyNumbersBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_place_canary:
                    return is.yranac.canary.databinding.FragmentSetupPlaceCanaryBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.listrow_location_select:
                    return is.yranac.canary.databinding.ListrowLocationSelectBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.layout_segmented:
                    return is.yranac.canary.databinding.LayoutSegmentedBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_failed_device_ota:
                    return is.yranac.canary.databinding.FragmentSettingsFailedDeviceOtaBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_membership_preview:
                    return is.yranac.canary.databinding.FragmentSetupMembershipPreviewBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_placement_suggestions:
                    return is.yranac.canary.databinding.FragmentSetupPlacementSuggestionsBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_select_location:
                    return is.yranac.canary.databinding.FragmentSetupSelectLocationBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_tutorial_mask_over:
                    return is.yranac.canary.databinding.FragmentTutorialMaskOverBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_add_a_member:
                    return is.yranac.canary.databinding.FragmentAddAMemberBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_get_help:
                    return is.yranac.canary.databinding.FragmentSetupGetHelpBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_adjust_volume:
                    return is.yranac.canary.databinding.FragmentSetupAdjustVolumeBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_location_primer:
                    return is.yranac.canary.databinding.FragmentSetupLocationPrimerBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.layout_sensor_flex:
                    return is.yranac.canary.databinding.LayoutSensorFlexBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragmentsettings_country_code_select:
                    return is.yranac.canary.databinding.FragmentsettingsCountryCodeSelectBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_mode_slide_show:
                    return is.yranac.canary.databinding.FragmentModeSlideShowBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.listview_device_motion_settings:
                    return is.yranac.canary.databinding.ListviewDeviceMotionSettingsBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_location:
                    return is.yranac.canary.databinding.FragmentSettingsLocationBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_check_geofence:
                    return is.yranac.canary.databinding.FragmentSetupCheckGeofenceBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_account:
                    return is.yranac.canary.databinding.FragmentSettingsAccountBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.alert_remove_layout:
                    return is.yranac.canary.databinding.AlertRemoveLayoutBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_connect_ethernet:
                    return is.yranac.canary.databinding.FragmentSetupConnectEthernetBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.footer_manage_devices:
                    return is.yranac.canary.databinding.FooterManageDevicesBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_connection_type:
                    return is.yranac.canary.databinding.FragmentSetupConnectionTypeBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_location_member:
                    return is.yranac.canary.databinding.FragmentSettingsLocationMemberBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_timeline_grid:
                    return is.yranac.canary.databinding.FragmentTimelineGridBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_timeline_overlay:
                    return is.yranac.canary.databinding.FragmentTimelineOverlayBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_membership_modes_upsell:
                    return is.yranac.canary.databinding.FragmentMembershipModesUpsellBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.map_header:
                    return is.yranac.canary.databinding.MapHeaderBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_location_list:
                    return is.yranac.canary.databinding.FragmentLocationListBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.activity_watch_live:
                    return is.yranac.canary.databinding.ActivityWatchLiveBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_main_layout:
                    return is.yranac.canary.databinding.FragmentMainLayoutBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.alert_dialog_new_generic:
                    return is.yranac.canary.databinding.AlertDialogNewGenericBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_geofence_radius:
                    return is.yranac.canary.databinding.FragmentSettingsGeofenceRadiusBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.settings_listrow_avatar_layout:
                    return is.yranac.canary.databinding.SettingsListrowAvatarLayoutBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_device_audio:
                    return is.yranac.canary.databinding.FragmentSetupDeviceAudioBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_confirm_network:
                    return is.yranac.canary.databinding.FragmentSetupConfirmNetworkBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_edit_device:
                    return is.yranac.canary.databinding.FragmentEditDeviceBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.menu_icon:
                    return is.yranac.canary.databinding.MenuIconBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_ota:
                    return is.yranac.canary.databinding.FragmentSetupOtaBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_buy_or_setup:
                    return is.yranac.canary.databinding.FragmentSetupBuyOrSetupBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_edit_location:
                    return is.yranac.canary.databinding.FragmentSettingsEditLocationBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.alert_dialog_free_trial:
                    return is.yranac.canary.databinding.AlertDialogFreeTrialBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.listrow_entry_simple:
                    return is.yranac.canary.databinding.ListrowEntrySimpleBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_edit_address:
                    return is.yranac.canary.databinding.FragmentSettingsEditAddressBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_create_current_user:
                    return is.yranac.canary.databinding.FragmentCreateCurrentUserBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.layout_mode_actions:
                    return is.yranac.canary.databinding.LayoutModeActionsBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.listraw_device_naming_footer:
                    return is.yranac.canary.databinding.ListrawDeviceNamingFooterBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.layout_siren_light:
                    return is.yranac.canary.databinding.LayoutSirenLightBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.listrow_entry_subscription_footer:
                    return is.yranac.canary.databinding.ListrowEntrySubscriptionFooterBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_indoor_outdoor_flex:
                    return is.yranac.canary.databinding.FragmentIndoorOutdoorFlexBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_membership_preview_over:
                    return is.yranac.canary.databinding.FragmentSettingsMembershipPreviewOverBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_web_view:
                    return is.yranac.canary.databinding.FragmentWebViewBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_about_device:
                    return is.yranac.canary.databinding.FragmentAboutDeviceBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_profile:
                    return is.yranac.canary.databinding.FragmentSettingsProfileBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_recording_range:
                    return is.yranac.canary.databinding.FragmentRecordingRangeBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.alert_dialog_sound_siren:
                    return is.yranac.canary.databinding.AlertDialogSoundSirenBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_edit_current_user:
                    return is.yranac.canary.databinding.FragmentEditCurrentUserBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_wifi_networks:
                    return is.yranac.canary.databinding.FragmentSetupWifiNetworksBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_preferences:
                    return is.yranac.canary.databinding.FragmentSettingsPreferencesBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.avatar_location_mode:
                    return is.yranac.canary.databinding.AvatarLocationModeBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_tutorial_initialize_motion:
                    return is.yranac.canary.databinding.FragmentTutorialInitializeMotionBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_device:
                    return is.yranac.canary.databinding.FragmentDeviceBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_single_entry_upsell:
                    return is.yranac.canary.databinding.FragmentSettingsSingleEntryUpsellBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.header_manage_devices:
                    return is.yranac.canary.databinding.HeaderManageDevicesBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.listrow_member_trail:
                    return is.yranac.canary.databinding.ListrowMemberTrailBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_location:
                    return is.yranac.canary.databinding.FragmentLocationBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_membership:
                    return is.yranac.canary.databinding.FragmentSetupMembershipBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.listrow_add_member_footer:
                    return is.yranac.canary.databinding.ListrowAddMemberFooterBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.layout_homehealth_header:
                    return is.yranac.canary.databinding.LayoutHomehealthHeaderBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_manage_devices:
                    return is.yranac.canary.databinding.FragmentManageDevicesBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.listview_device_mode_settings:
                    return is.yranac.canary.databinding.ListviewDeviceModeSettingsBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_video_playback:
                    return is.yranac.canary.databinding.FragmentVideoPlaybackBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_find_bluetooth_devices:
                    return is.yranac.canary.databinding.FragmentFindBluetoothDevicesBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_settings_notifcations:
                    return is.yranac.canary.databinding.FragmentSettingsNotifcationsBinding.bind(view, bindingComponent);
                case is.yranac.canary.R.layout.fragment_setup_forgot_password:
                    return is.yranac.canary.databinding.FragmentSetupForgotPasswordBinding.bind(view, bindingComponent);
        }
        return null;
    }
    android.databinding.ViewDataBinding getDataBinder(android.databinding.DataBindingComponent bindingComponent, android.view.View[] views, int layoutId) {
        switch(layoutId) {
        }
        return null;
    }
    int getLayoutId(String tag) {
        if (tag == null) {
            return 0;
        }
        final int code = tag.hashCode();
        switch(code) {
            case 1763108930: {
                if(tag.equals("layout/fragment_settings_home_mode_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_home_mode;
                }
                break;
            }
            case -1044576432: {
                if(tag.equals("layout/fragment_settings_device_naming_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_device_naming;
                }
                break;
            }
            case 304674572: {
                if(tag.equals("layout/fragment_add_a_location_0")) {
                    return is.yranac.canary.R.layout.fragment_add_a_location;
                }
                break;
            }
            case -1933906334: {
                if(tag.equals("layout/footer_remove_location_0")) {
                    return is.yranac.canary.R.layout.footer_remove_location;
                }
                break;
            }
            case -1959955454: {
                if(tag.equals("layout/fragment_settings_location_overview_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_location_overview;
                }
                break;
            }
            case 1329432956: {
                if(tag.equals("layout/activity_masking_0")) {
                    return is.yranac.canary.R.layout.activity_masking;
                }
                break;
            }
            case -2038515578: {
                if(tag.equals("layout/fragment_health_graph_0")) {
                    return is.yranac.canary.R.layout.fragment_health_graph;
                }
                break;
            }
            case 395505866: {
                if(tag.equals("layout/fragment_settings_simple_list_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_simple_list;
                }
                break;
            }
            case -2039758672: {
                if(tag.equals("layout/fragment_main_settings_0")) {
                    return is.yranac.canary.R.layout.fragment_main_settings;
                }
                break;
            }
            case -2047029190: {
                if(tag.equals("layout/activity_entry_detail_0")) {
                    return is.yranac.canary.R.layout.activity_entry_detail;
                }
                break;
            }
            case 1466367681: {
                if(tag.equals("layout/new_layout_video_playback_0")) {
                    return is.yranac.canary.R.layout.new_layout_video_playback;
                }
                break;
            }
            case -543321235: {
                if(tag.equals("layout/fragment_setup_connect_cable_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_connect_cable;
                }
                break;
            }
            case 1662471064: {
                if(tag.equals("layout/lte_level_view_0")) {
                    return is.yranac.canary.R.layout.lte_level_view;
                }
                break;
            }
            case 423333129: {
                if(tag.equals("layout/fragment_setup_splash_screen_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_splash_screen;
                }
                break;
            }
            case 1904273743: {
                if(tag.equals("layout/alert_dialog_permission_0")) {
                    return is.yranac.canary.R.layout.alert_dialog_permission;
                }
                break;
            }
            case -327685220: {
                if(tag.equals("layout/avatar_layout_0")) {
                    return is.yranac.canary.R.layout.avatar_layout;
                }
                break;
            }
            case 2087203717: {
                if(tag.equals("layout/layout_about_device_header_0")) {
                    return is.yranac.canary.R.layout.layout_about_device_header;
                }
                break;
            }
            case 988461733: {
                if(tag.equals("layout/fragment_entrydetail_tag_0")) {
                    return is.yranac.canary.R.layout.fragment_entrydetail_tag;
                }
                break;
            }
            case 1761185197: {
                if(tag.equals("layout/fragment_settings_get_help_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_get_help;
                }
                break;
            }
            case 1883933447: {
                if(tag.equals("layout/fragment_settings_geofence_position_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_geofence_position;
                }
                break;
            }
            case -782884999: {
                if(tag.equals("layout/fragment_settings_membership_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_membership;
                }
                break;
            }
            case 163656135: {
                if(tag.equals("layout/fragment_settings_presence_notifications_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_presence_notifications;
                }
                break;
            }
            case 239727356: {
                if(tag.equals("layout/fragment_settings_flex_connectivity_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_flex_connectivity;
                }
                break;
            }
            case 755099211: {
                if(tag.equals("layout/fragment_power_battery_notif_0")) {
                    return is.yranac.canary.R.layout.fragment_power_battery_notif;
                }
                break;
            }
            case -2117872007: {
                if(tag.equals("layout/fragment_tutorial_0")) {
                    return is.yranac.canary.R.layout.fragment_tutorial;
                }
                break;
            }
            case 208848782: {
                if(tag.equals("layout/layout_header_0")) {
                    return is.yranac.canary.R.layout.layout_header;
                }
                break;
            }
            case 328494858: {
                if(tag.equals("layout/layout_device_statatics_0")) {
                    return is.yranac.canary.R.layout.layout_device_statatics;
                }
                break;
            }
            case 364843385: {
                if(tag.equals("layout/view_battery_indicator_0")) {
                    return is.yranac.canary.R.layout.view_battery_indicator;
                }
                break;
            }
            case -618628130: {
                if(tag.equals("layout/fragment_location_services_debugging_0")) {
                    return is.yranac.canary.R.layout.fragment_location_services_debugging;
                }
                break;
            }
            case -489008333: {
                if(tag.equals("layout/alert_dialog_add_person_0")) {
                    return is.yranac.canary.R.layout.alert_dialog_add_person;
                }
                break;
            }
            case 770927899: {
                if(tag.equals("layout/change_password_fragment_0")) {
                    return is.yranac.canary.R.layout.change_password_fragment;
                }
                break;
            }
            case -1160262573: {
                if(tag.equals("layout/fragment_tutorial_masking_start_0")) {
                    return is.yranac.canary.R.layout.fragment_tutorial_masking_start;
                }
                break;
            }
            case 1741160959: {
                if(tag.equals("layout/fragment_tutorial_setup_masks_0")) {
                    return is.yranac.canary.R.layout.fragment_tutorial_setup_masks;
                }
                break;
            }
            case 470007141: {
                if(tag.equals("layout/listrow_send_feedback_0")) {
                    return is.yranac.canary.R.layout.listrow_send_feedback;
                }
                break;
            }
            case -418574980: {
                if(tag.equals("layout/layout_homehealth_0")) {
                    return is.yranac.canary.R.layout.layout_homehealth;
                }
                break;
            }
            case -1526584174: {
                if(tag.equals("layout/fragment_motion_notificaions_settings_0")) {
                    return is.yranac.canary.R.layout.fragment_motion_notificaions_settings;
                }
                break;
            }
            case -1478926032: {
                if(tag.equals("layout/fragment_setup_start_membership_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_start_membership;
                }
                break;
            }
            case 1725222679: {
                if(tag.equals("layout/activity_setup_fragment_stack_0")) {
                    return is.yranac.canary.R.layout.activity_setup_fragment_stack;
                }
                break;
            }
            case 1698204061: {
                if(tag.equals("layout/select_device_location_header_0")) {
                    return is.yranac.canary.R.layout.select_device_location_header;
                }
                break;
            }
            case -1890556274: {
                if(tag.equals("layout/edit_text_with_label_0")) {
                    return is.yranac.canary.R.layout.edit_text_with_label;
                }
                break;
            }
            case 1612773606: {
                if(tag.equals("layout/layout_membership_benefits_0")) {
                    return is.yranac.canary.R.layout.layout_membership_benefits;
                }
                break;
            }
            case 318181254: {
                if(tag.equals("layout/fragment_settings_mode_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_mode;
                }
                break;
            }
            case -818195165: {
                if(tag.equals("layout/fragment_settings_geofence_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_geofence;
                }
                break;
            }
            case 1319058008: {
                if(tag.equals("layout/alert_dialog_terms_and_conditions_0")) {
                    return is.yranac.canary.R.layout.alert_dialog_terms_and_conditions;
                }
                break;
            }
            case -82522146: {
                if(tag.equals("layout/alert_dialog_emergency_numbers_0")) {
                    return is.yranac.canary.R.layout.alert_dialog_emergency_numbers;
                }
                break;
            }
            case 661203693: {
                if(tag.equals("layout/fragment_setup_place_canary_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_place_canary;
                }
                break;
            }
            case 900315983: {
                if(tag.equals("layout/listrow_location_select_0")) {
                    return is.yranac.canary.R.layout.listrow_location_select;
                }
                break;
            }
            case 566978483: {
                if(tag.equals("layout/layout_segmented_0")) {
                    return is.yranac.canary.R.layout.layout_segmented;
                }
                break;
            }
            case -1448058956: {
                if(tag.equals("layout/fragment_settings_failed_device_ota_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_failed_device_ota;
                }
                break;
            }
            case -2052687940: {
                if(tag.equals("layout/fragment_setup_membership_preview_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_membership_preview;
                }
                break;
            }
            case 1308819482: {
                if(tag.equals("layout/fragment_setup_placement_suggestions_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_placement_suggestions;
                }
                break;
            }
            case 2007329117: {
                if(tag.equals("layout/fragment_setup_select_location_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_select_location;
                }
                break;
            }
            case 1832863553: {
                if(tag.equals("layout/fragment_tutorial_mask_over_0")) {
                    return is.yranac.canary.R.layout.fragment_tutorial_mask_over;
                }
                break;
            }
            case -695419343: {
                if(tag.equals("layout/fragment_add_a_member_0")) {
                    return is.yranac.canary.R.layout.fragment_add_a_member;
                }
                break;
            }
            case 807793191: {
                if(tag.equals("layout/fragment_setup_get_help_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_get_help;
                }
                break;
            }
            case 181075983: {
                if(tag.equals("layout/fragment_setup_adjust_volume_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_adjust_volume;
                }
                break;
            }
            case -1907008990: {
                if(tag.equals("layout/fragment_setup_location_primer_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_location_primer;
                }
                break;
            }
            case -1107204353: {
                if(tag.equals("layout/layout_sensor_flex_0")) {
                    return is.yranac.canary.R.layout.layout_sensor_flex;
                }
                break;
            }
            case 1241870479: {
                if(tag.equals("layout/fragmentsettings_country_code_select_0")) {
                    return is.yranac.canary.R.layout.fragmentsettings_country_code_select;
                }
                break;
            }
            case -400901970: {
                if(tag.equals("layout/fragment_mode_slide_show_0")) {
                    return is.yranac.canary.R.layout.fragment_mode_slide_show;
                }
                break;
            }
            case 1763216491: {
                if(tag.equals("layout/listview_device_motion_settings_0")) {
                    return is.yranac.canary.R.layout.listview_device_motion_settings;
                }
                break;
            }
            case -1851690984: {
                if(tag.equals("layout/fragment_settings_location_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_location;
                }
                break;
            }
            case 1197847604: {
                if(tag.equals("layout/fragment_setup_check_geofence_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_check_geofence;
                }
                break;
            }
            case 141711980: {
                if(tag.equals("layout/fragment_settings_account_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_account;
                }
                break;
            }
            case 715799470: {
                if(tag.equals("layout/alert_remove_layout_0")) {
                    return is.yranac.canary.R.layout.alert_remove_layout;
                }
                break;
            }
            case 1610997417: {
                if(tag.equals("layout/fragment_setup_connect_ethernet_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_connect_ethernet;
                }
                break;
            }
            case -1898585805: {
                if(tag.equals("layout/footer_manage_devices_0")) {
                    return is.yranac.canary.R.layout.footer_manage_devices;
                }
                break;
            }
            case -1558198016: {
                if(tag.equals("layout/fragment_setup_connection_type_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_connection_type;
                }
                break;
            }
            case 655274723: {
                if(tag.equals("layout/fragment_settings_location_member_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_location_member;
                }
                break;
            }
            case 1242082955: {
                if(tag.equals("layout/fragment_timeline_grid_0")) {
                    return is.yranac.canary.R.layout.fragment_timeline_grid;
                }
                break;
            }
            case -284024211: {
                if(tag.equals("layout/fragment_timeline_overlay_0")) {
                    return is.yranac.canary.R.layout.fragment_timeline_overlay;
                }
                break;
            }
            case -54519700: {
                if(tag.equals("layout/fragment_membership_modes_upsell_0")) {
                    return is.yranac.canary.R.layout.fragment_membership_modes_upsell;
                }
                break;
            }
            case -1243710138: {
                if(tag.equals("layout/map_header_0")) {
                    return is.yranac.canary.R.layout.map_header;
                }
                break;
            }
            case 1088292847: {
                if(tag.equals("layout/fragment_location_list_0")) {
                    return is.yranac.canary.R.layout.fragment_location_list;
                }
                break;
            }
            case 1896906008: {
                if(tag.equals("layout/activity_watch_live_0")) {
                    return is.yranac.canary.R.layout.activity_watch_live;
                }
                break;
            }
            case -1190588169: {
                if(tag.equals("layout/fragment_main_layout_0")) {
                    return is.yranac.canary.R.layout.fragment_main_layout;
                }
                break;
            }
            case -2012890566: {
                if(tag.equals("layout/alert_dialog_new_generic_0")) {
                    return is.yranac.canary.R.layout.alert_dialog_new_generic;
                }
                break;
            }
            case 604069744: {
                if(tag.equals("layout/fragment_settings_geofence_radius_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_geofence_radius;
                }
                break;
            }
            case -1189748505: {
                if(tag.equals("layout/settings_listrow_avatar_layout_0")) {
                    return is.yranac.canary.R.layout.settings_listrow_avatar_layout;
                }
                break;
            }
            case 670582154: {
                if(tag.equals("layout/fragment_setup_device_audio_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_device_audio;
                }
                break;
            }
            case 1154314164: {
                if(tag.equals("layout/fragment_setup_confirm_network_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_confirm_network;
                }
                break;
            }
            case -1529709582: {
                if(tag.equals("layout/fragment_edit_device_0")) {
                    return is.yranac.canary.R.layout.fragment_edit_device;
                }
                break;
            }
            case 424011109: {
                if(tag.equals("layout/menu_icon_0")) {
                    return is.yranac.canary.R.layout.menu_icon;
                }
                break;
            }
            case -764523039: {
                if(tag.equals("layout/fragment_setup_ota_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_ota;
                }
                break;
            }
            case 726110295: {
                if(tag.equals("layout/fragment_setup_buy_or_setup_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_buy_or_setup;
                }
                break;
            }
            case -1515112247: {
                if(tag.equals("layout/fragment_settings_edit_location_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_edit_location;
                }
                break;
            }
            case -827421181: {
                if(tag.equals("layout/alert_dialog_free_trial_0")) {
                    return is.yranac.canary.R.layout.alert_dialog_free_trial;
                }
                break;
            }
            case -1485773640: {
                if(tag.equals("layout/listrow_entry_simple_0")) {
                    return is.yranac.canary.R.layout.listrow_entry_simple;
                }
                break;
            }
            case -1025182814: {
                if(tag.equals("layout/fragment_settings_edit_address_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_edit_address;
                }
                break;
            }
            case 1996662459: {
                if(tag.equals("layout/fragment_create_current_user_0")) {
                    return is.yranac.canary.R.layout.fragment_create_current_user;
                }
                break;
            }
            case -116888990: {
                if(tag.equals("layout/layout_mode_actions_0")) {
                    return is.yranac.canary.R.layout.layout_mode_actions;
                }
                break;
            }
            case -201827052: {
                if(tag.equals("layout/listraw_device_naming_footer_0")) {
                    return is.yranac.canary.R.layout.listraw_device_naming_footer;
                }
                break;
            }
            case 1055275261: {
                if(tag.equals("layout/layout_siren_light_0")) {
                    return is.yranac.canary.R.layout.layout_siren_light;
                }
                break;
            }
            case -1747393095: {
                if(tag.equals("layout/listrow_entry_subscription_footer_0")) {
                    return is.yranac.canary.R.layout.listrow_entry_subscription_footer;
                }
                break;
            }
            case -1373359793: {
                if(tag.equals("layout/fragment_indoor_outdoor_flex_0")) {
                    return is.yranac.canary.R.layout.fragment_indoor_outdoor_flex;
                }
                break;
            }
            case -532157645: {
                if(tag.equals("layout/fragment_settings_membership_preview_over_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_membership_preview_over;
                }
                break;
            }
            case -2064270517: {
                if(tag.equals("layout/fragment_web_view_0")) {
                    return is.yranac.canary.R.layout.fragment_web_view;
                }
                break;
            }
            case -1542053149: {
                if(tag.equals("layout/fragment_about_device_0")) {
                    return is.yranac.canary.R.layout.fragment_about_device;
                }
                break;
            }
            case 963341032: {
                if(tag.equals("layout/fragment_settings_profile_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_profile;
                }
                break;
            }
            case -2016423114: {
                if(tag.equals("layout/fragment_recording_range_0")) {
                    return is.yranac.canary.R.layout.fragment_recording_range;
                }
                break;
            }
            case -2025450473: {
                if(tag.equals("layout/alert_dialog_sound_siren_0")) {
                    return is.yranac.canary.R.layout.alert_dialog_sound_siren;
                }
                break;
            }
            case 736631149: {
                if(tag.equals("layout/fragment_edit_current_user_0")) {
                    return is.yranac.canary.R.layout.fragment_edit_current_user;
                }
                break;
            }
            case 285360884: {
                if(tag.equals("layout/fragment_setup_wifi_networks_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_wifi_networks;
                }
                break;
            }
            case -666300041: {
                if(tag.equals("layout/fragment_settings_preferences_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_preferences;
                }
                break;
            }
            case -1699275267: {
                if(tag.equals("layout/avatar_location_mode_0")) {
                    return is.yranac.canary.R.layout.avatar_location_mode;
                }
                break;
            }
            case 1892635583: {
                if(tag.equals("layout/fragment_tutorial_initialize_motion_0")) {
                    return is.yranac.canary.R.layout.fragment_tutorial_initialize_motion;
                }
                break;
            }
            case -1428185615: {
                if(tag.equals("layout/fragment_device_0")) {
                    return is.yranac.canary.R.layout.fragment_device;
                }
                break;
            }
            case 976973232: {
                if(tag.equals("layout/fragment_settings_single_entry_upsell_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_single_entry_upsell;
                }
                break;
            }
            case -1216022911: {
                if(tag.equals("layout/header_manage_devices_0")) {
                    return is.yranac.canary.R.layout.header_manage_devices;
                }
                break;
            }
            case -353022406: {
                if(tag.equals("layout/listrow_member_trail_0")) {
                    return is.yranac.canary.R.layout.listrow_member_trail;
                }
                break;
            }
            case -1631416048: {
                if(tag.equals("layout/fragment_location_0")) {
                    return is.yranac.canary.R.layout.fragment_location;
                }
                break;
            }
            case 2130398579: {
                if(tag.equals("layout/fragment_setup_membership_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_membership;
                }
                break;
            }
            case 1190382923: {
                if(tag.equals("layout/listrow_add_member_footer_0")) {
                    return is.yranac.canary.R.layout.listrow_add_member_footer;
                }
                break;
            }
            case -2080758734: {
                if(tag.equals("layout/layout_homehealth_header_0")) {
                    return is.yranac.canary.R.layout.layout_homehealth_header;
                }
                break;
            }
            case 1065253470: {
                if(tag.equals("layout/fragment_manage_devices_0")) {
                    return is.yranac.canary.R.layout.fragment_manage_devices;
                }
                break;
            }
            case -1595867170: {
                if(tag.equals("layout/listview_device_mode_settings_0")) {
                    return is.yranac.canary.R.layout.listview_device_mode_settings;
                }
                break;
            }
            case 1915790522: {
                if(tag.equals("layout/fragment_video_playback_0")) {
                    return is.yranac.canary.R.layout.fragment_video_playback;
                }
                break;
            }
            case 995474145: {
                if(tag.equals("layout/fragment_find_bluetooth_devices_0")) {
                    return is.yranac.canary.R.layout.fragment_find_bluetooth_devices;
                }
                break;
            }
            case -1063589516: {
                if(tag.equals("layout/fragment_settings_notifcations_0")) {
                    return is.yranac.canary.R.layout.fragment_settings_notifcations;
                }
                break;
            }
            case 752559676: {
                if(tag.equals("layout/fragment_setup_forgot_password_0")) {
                    return is.yranac.canary.R.layout.fragment_setup_forgot_password;
                }
                break;
            }
        }
        return 0;
    }
    String convertBrIdToString(int id) {
        if (id < 0 || id >= InnerBrLookup.sKeys.length) {
            return null;
        }
        return InnerBrLookup.sKeys[id];
    }
    private static class InnerBrLookup {
        static String[] sKeys = new String[]{
            "_all"
            ,"addEnabled"
            ,"controlListener"
            ,"deleteEnabled"
            ,"device"
            ,"deviceType"
            ,"entry"
            ,"handlers"
            ,"isLocationSetup"
            ,"isSetup"
            ,"location"
            ,"multipleMasks"
            ,"numberOfMasks"
            ,"saveEnabled"
            ,"showOptions"
            ,"stringTypes"
            ,"subscription"
            ,"topMessage"
            ,"tutorialType"
            ,"v"};
    }
}