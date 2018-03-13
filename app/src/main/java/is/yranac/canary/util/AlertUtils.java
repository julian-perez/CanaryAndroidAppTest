package is.yranac.canary.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import is.yranac.canary.Constants;
import is.yranac.canary.R;
import is.yranac.canary.databinding.AlertDialogAddPersonBinding;
import is.yranac.canary.databinding.AlertDialogEmergencyNumbersBinding;
import is.yranac.canary.databinding.AlertDialogFreeTrialBinding;
import is.yranac.canary.databinding.AlertDialogNewGenericBinding;
import is.yranac.canary.databinding.AlertDialogPermissionBinding;
import is.yranac.canary.databinding.AlertDialogSoundSirenBinding;
import is.yranac.canary.databinding.AlertRemoveLayoutBinding;
import is.yranac.canary.interfaces.DeviceActivationDialogListener;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.model.device.DeviceType;
import is.yranac.canary.model.emergencycontacts.EmergencyContact;
import is.yranac.canary.model.location.Location;
import is.yranac.canary.model.subscription.ServiceProfile;
import is.yranac.canary.model.subscription.Subscription;
import is.yranac.canary.model.videoexport.VideoExport;
import is.yranac.canary.services.api.CustomerAPIService;
import is.yranac.canary.services.api.LocationAPIService;
import is.yranac.canary.services.database.EmergencyContactDatabaseService;
import is.yranac.canary.services.database.SubscriptionPlanDatabaseService;
import is.yranac.canary.services.intent.GooglePlacesAPIIntentServiceEmergencyNumbers;
import is.yranac.canary.ui.BaseActivity;
import is.yranac.canary.ui.SettingsFragmentStackActivity;
import is.yranac.canary.util.cache.location.UpdateDataOptIn;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static is.yranac.canary.ui.SettingsFragmentStackActivity.invite_members;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_CALL_CANCEL;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_CALL_EMS;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_CALL_FIRE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_CALL_POLICE;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_SIREN_CANCEL;
import static is.yranac.canary.util.ga.AnalyticsConstants.CALL_PRESS;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_EMERGENCY_OPTIONS;

/**
 * Created by Schroeder on 12/13/14.
 */
public class AlertUtils {


    private static final String LOG_TAG = "AlertUtils";

    public static AlertDialog showRemoveAlert(Context context, String title, final View.OnClickListener clickListener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        AlertRemoveLayoutBinding binding = AlertRemoveLayoutBinding.inflate(inflater);
        final AlertDialog dialog = buildAlert(context, binding.getRoot(), false);

        binding.alertTitle.setText(title);
        binding.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                clickListener.onClick(v);
            }
        });


        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        return dialog;
    }

    public static AlertDialog showWifiAlert(Context context, View.OnClickListener onClickListener) {
        if (context == null)
            return null;

        String title = context.getString(R.string.location_accuracy);
        String dsc = context.getString(R.string.location_accuracy_dsc);
        int image = R.drawable.wifi_signal_illustration;
        String leftBtnString = context.getString(R.string.go_to_settings);
        String rightBtnString = context.getString(R.string.okay);

        return AlertUtils.showGenericAlert(context, title, dsc, image, leftBtnString, rightBtnString, 0, 0, onClickListener, null);
    }

    public static AlertDialog showProcessingVideoAlert(Context context, boolean share, List<VideoExport> videoExports) {
        if (context == null)
            return null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View alertView = inflater.inflate(R.layout.alert_dialog_processing_video, null);


        int duration = 0;

        float size = 0;

        for (VideoExport videoExport : videoExports) {
            size += videoExport.size;
            duration += videoExport.duration;
        }

        float sizeMB = (int) (size / Constants.Kibi);

        float durationSec = duration % 60;
        float durationMin = duration / 60;

        DecimalFormat format = new DecimalFormat("##");
        format.setMinimumIntegerDigits(2);

        format.setRoundingMode(RoundingMode.HALF_UP);
        TextView videoLengthTextView = (TextView) alertView.findViewById(R.id.video_length_text_view);
        String time = context.getString(R.string.time_format, format.format(durationMin), format.format(durationSec));
        videoLengthTextView.setText(time);

        TextView videoSizeTextView = (TextView) alertView.findViewById(R.id.video_size_text_view);
        format.setMinimumIntegerDigits(1);

        if (size < Constants.Kibi) {
            videoSizeTextView.setText(context.getString(R.string.kb_format, format.format(size)));
        } else {
            videoSizeTextView.setText(context.getString(R.string.mb_format, format.format(sizeMB)));
        }

        TextView headerTextView = (TextView) alertView.findViewById(R.id.alert_header);
        TextView descriptionTextView = (TextView) alertView.findViewById(R.id.alert_desc);

        if (share) {
            headerTextView.setText(R.string.processing_share);
            descriptionTextView.setText(R.string.processing_share_dsc);
        }

        final AlertDialog alert = buildAlert(context, alertView, true);
        alert.show();

        Button okayButton = (Button) alert.findViewById(R.id.okay_button);
        okayButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();

                    }
                });

        return alert;

    }

    public static AlertDialog showDataOptin(Context context, final Location location) {
        if (context == null)
            return null;

        CurrentCustomer.getCurrentCustomer().seenSharePrompt = true;
        CustomerAPIService.editCustomer(CurrentCustomer.getCurrentCustomer(), new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


        LayoutInflater inflater = LayoutInflater.from(context);

        View alertView = inflater.inflate(R.layout.data_onboarding_fragment, null);


        Button leftBtn = (Button) alertView.findViewById(R.id.alert_button_left);

        Button rightBtn = (Button) alertView.findViewById(R.id.alert_button_right);

        final AlertDialog alert = buildAlert(context, alertView, false);
        alert.show();


        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationAPIService.changeDataOptIn(location.id, true, new Callback<Void>() {
                    @Override
                    public void success(Void aVoid, Response response) {
                        TinyMessageBus.post(new UpdateDataOptIn(true, location.id));
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
                alert.dismiss();
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        return alert;
    }

    public static AlertDialog showDwonloadWifiAlert(Context context, final View.OnClickListener downloadListener) {
        if (context == null)
            return null;

        return showGenericAlert(context, context.getString(R.string.wifi_recommended), context.getString(R.string.wifi_recommended_dsc),
                R.drawable.icon_wifi, context.getString(R.string.download), context.getString(R.string.cancel),
                0, 0, downloadListener, null);
    }


    public static AlertDialog showUnSaveWarningAlert(Context context, Date startDate, View.OnClickListener onClickListener) {
        if (context == null)
            return null;
        Calendar cal = DateUtil.getCalanderInstance();

        String unsaveConfirm = context.getString(R.string.remove_this_bookmark);
        String olderThan;
        Subscription subscription = SubscriptionPlanDatabaseService.getServicePlanForCurrentLocation();
        if (subscription == null) {
            olderThan = context.getString(R.string.entry_older_than);
        } else {

            ServiceProfile serviceProfile = subscription.currentServiceProfile;
            cal.add(Calendar.HOUR, serviceProfile.timeLineLength * -1);
            Date oldest = cal.getTime();

            if (startDate.after(oldest)) {
                if (serviceProfile.timeLineLength > 24) {
                    olderThan = context.getString(R.string.unsave_confirmation_dsc_days, serviceProfile.timeLineLength / 24);
                } else {
                    olderThan = context.getString(R.string.unsave_confirmation_dsc_hours, serviceProfile.timeLineLength);
                }
            } else {
                if (serviceProfile.timeLineLength > 24) {
                    olderThan = context.getString(R.string.entry_older_than_days, serviceProfile.timeLineLength / 24);
                } else {
                    olderThan = context.getString(R.string.entry_older_than_hours, serviceProfile.timeLineLength);
                }
            }

        }

        String unsave = context.getString(R.string.remove_bookmark);

        String cancel = context.getString(R.string.cancel);
        int redColor = startDate.after(cal.getTime()) ? 0 : context.getResources().getColor(R.color.light_red);
        return showGenericAlert(context, unsaveConfirm, olderThan, 0, unsave, cancel, redColor, 0, onClickListener, null);
    }

    public static void showEmergencyContactUpdateAlert(final Activity context, final int locationId, final View.OnClickListener clickListener) {
        LayoutInflater inflater = LayoutInflater.from(context);


        View alertView = inflater.inflate(R.layout.alert_dialog_update_emergency_contacts, null);

        final AlertDialog alert = buildAlert(context, alertView, true);

        Button leftBtn = (Button) alertView.findViewById(R.id.alert_button_left);

        Button rightBtn = (Button) alertView.findViewById(R.id.alert_button_right);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.onClick(v);
                GooglePlacesAPIIntentServiceEmergencyNumbers.getNewEmergencyNumbersForLocations(locationId, false);
                alert.dismiss();
            }
        });

        alert.show();
    }

    public static AlertDialog showLocaleAlert(Context context, View.OnClickListener onClickListener) {
        Locale currentLocale = Resources.getSystem().getConfiguration().locale;
        String nowAvailable = context.getString(R.string.language_now_available, LocaleHelper.getDisplayLanguage(context, currentLocale));
        String nowAvailableDsc = context.getString(R.string.language_now_available_dsc, LocaleHelper.getDisplayLanguage(context, currentLocale));
        AlertDialog dialog = showGenericAlert(context, nowAvailable, nowAvailableDsc, 0, context.getString(R.string.update), context.getString(R.string.cancel), 0, 0, onClickListener, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesUtils.setHasSeenLanguagePrompt();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                PreferencesUtils.setHasSeenLanguagePrompt();
            }
        });

        return dialog;
    }

    public static AlertDialog showLoggedOut(final Context context) {

        String loggedOut = context.getString(R.string.you_ve_been_logged_out);
        AlertDialog dialog = showGenericAlert(context, loggedOut, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutUtil.continueLogout(context, false);

            }
        });

        dialog.setCancelable(false);

        return dialog;

    }

    public static AlertDialog showSirenAlert(Context context, boolean hasSiren, int sirenCounts, int deviceType,
                                             final View.OnClickListener soundAllClickListener,
                                             final View.OnClickListener soundThisClickListner) {

        if (context == null)
            return null;

        LayoutInflater inflater = LayoutInflater.from(context);
        AlertDialogSoundSirenBinding binding = AlertDialogSoundSirenBinding.inflate(inflater);

        final AlertDialog alert = buildAlert(context, binding.getRoot(), true);
        alert.show();

        binding.soundSirenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                soundAllClickListener.onClick(v);
            }
        });

        if (hasSiren && sirenCounts > 1) {
            binding.soundOneSirenBtn.setVisibility(View.VISIBLE);
            binding.soundOneSirenBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alert.dismiss();

                    soundThisClickListner.onClick(v);
                }
            });
        } else if (hasSiren && sirenCounts == 1) {
            binding.soundSirenBtn.setText(R.string.sound_siren);
        } else if (!hasSiren) {
            binding.doesntHaveSirenLabel.setVisibility(View.VISIBLE);
            switch (deviceType) {
                case DeviceType.CANARY_VIEW:
                    binding.doesntHaveSirenLabel.setText(R.string.view_doesnt_have_siren);
                    break;
                case DeviceType.FLEX:
                    binding.doesntHaveSirenLabel.setText(R.string.flex_doesnt_have_siren);
                    break;
            }
        }

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_EMERGENCY_OPTIONS, ACTION_SIREN_CANCEL);
                alert.dismiss();
            }
        });


        Window window = alert.getWindow();
        if (window != null) {
            WindowManager.LayoutParams wlp = window.getAttributes();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            wlp.dimAmount = 0.85f;

            window.setAttributes(wlp);
        }
        return alert;
    }

    public static AlertDialog showPermissionOverlay(final Context context, final String permission) {
        if (context == null)
            return null;

        String permissionName;


        try {
            PackageManager packageManager = context.getApplicationContext().getPackageManager();

            PermissionInfo permissionInfo = packageManager.getPermissionInfo(permission, 0);
            PermissionGroupInfo permissionGroupInfo = packageManager.getPermissionGroupInfo(permissionInfo.group, 0);
            permissionName = permissionGroupInfo.loadLabel(packageManager).toString().toLowerCase();
        } catch (PackageManager.NameNotFoundException e) {
            return null;

        }
        LayoutInflater inflater = LayoutInflater.from(context);

        String allowPermission = context.getString(R.string.allow_permission_access, permissionName);

        AlertDialogPermissionBinding binding = AlertDialogPermissionBinding.inflate(inflater);
        binding.alertHeader.setText(allowPermission);
        final AlertDialog alert = buildAlert(context, binding.getRoot(), true);

        binding.gotToPermissionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);
                alert.dismiss();
            }
        });

        binding.notNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.show();

        return alert;

    }

    public static AlertDialog showNewGenericAlert(Context context, String header,
                                                  String topButtonText, String bottomButtonText,
                                                  final View.OnClickListener leftListener, final View.OnClickListener rightListener) {

        if (context == null)
            return null;

        if ("".equalsIgnoreCase(header))
            return null;
        LayoutInflater inflater = LayoutInflater.from(context);
        AlertDialogNewGenericBinding binding = AlertDialogNewGenericBinding.inflate(inflater);


        if (header != null)
            binding.alertTitle.setText(header);
        else
            binding.alertTitle.setVisibility(View.GONE);


        binding.removeBtn.setText(topButtonText);
        binding.cancelBtn.setText(bottomButtonText);


        final AlertDialog alert = buildAlert(context, binding.getRoot(), true);

        alert.show();
        binding.removeBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        if (leftListener != null) {
                            leftListener.onClick(v);
                        }
                    }
                });

        binding.cancelBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        if (rightListener != null) {
                            rightListener.onClick(v);
                        }

                    }
                });


        return alert;

    }

    public static AlertDialog showNewGreenGenericAlert(Context context, String header, String desc,
                                                       String topButtonText, String bottomButtonText,
                                                       final View.OnClickListener leftListener, final View.OnClickListener rightListener) {

        return showNewGenericAlert(context, header, desc, topButtonText, bottomButtonText,
                R.color.white, R.drawable.green_filled_background_rect, leftListener, rightListener);


    }


    public static AlertDialog showNewGenericAlert(Context context, String header, String desc, String topButtonText, String bottomButtonText,
                                                  @ColorRes int topTextColor, @DrawableRes int topBtnDrawable,
                                                  final View.OnClickListener leftListener, final View.OnClickListener rightListener) {

        if (context == null)
            return null;

        if (TextUtils.isEmpty(header))
            return null;

        LayoutInflater inflater = LayoutInflater.from(context);
        AlertDialogNewGenericBinding binding = AlertDialogNewGenericBinding.inflate(inflater);


        binding.alertTitle.setText(header);

        if (desc != null) {
            binding.alertDsc.setText(desc);
        } else {
            binding.alertDsc.setVisibility(View.GONE);
        }


        binding.removeBtn.setText(topButtonText);
        binding.removeBtn.setBackgroundResource(topBtnDrawable);
        binding.removeBtn.setTextColor(ContextCompat.getColor(context, topTextColor));
        binding.cancelBtn.setText(bottomButtonText);


        final AlertDialog alert = buildAlert(context, binding.getRoot(), true);

        alert.show();
        binding.removeBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        if (leftListener != null) {
                            leftListener.onClick(v);
                        }
                    }
                });

        binding.cancelBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        if (rightListener != null) {
                            rightListener.onClick(v);
                        }

                    }
                });


        return alert;
    }

    public static AlertDialog activateMembershipFreeTrail(Context context, boolean threeMonths, final View.OnClickListener start,
                                                          final View.OnClickListener skip) {
        LayoutInflater inflater = LayoutInflater.from(context);

        AlertDialogFreeTrialBinding binding = AlertDialogFreeTrialBinding.inflate(inflater);
        final AlertDialog alert = buildAlert(context, binding.getRoot(), false);

        if (threeMonths) {
            binding.promoDscTextView.setText(R.string.your_three_month_trial_includes);
            binding.startFreeTrialBtn.setText(R.string.activate_membership);
            binding.skipFreeTrialBtn.setText(R.string.no_thanks);
        }

        alert.show();

        binding.skipFreeTrialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                skip.onClick(view);
            }
        });

        binding.startFreeTrialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                start.onClick(view);
            }
        });

        return alert;
    }

    public interface OnClickListenerWithText {
        void onClick(View v, String text);
    }

    public static AlertDialog showWifiPasswordAlert(final Context context, final OnClickListenerWithText retryClickListener
            , final View.OnClickListener startOverClickListener) {
        if (context == null)
            return null;

        LayoutInflater inflater = LayoutInflater.from(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alert = builder.create();

        View customAlertView = inflater.inflate(R.layout.alert_dialog_wifi_password, null);


        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        alert.setView(customAlertView, 0, 0, 0, 0);
        alert.show();
        final EditText editText = (EditText) alert.findViewById(R.id.wifi_password_edit_text);

        final Button startOverBtn = (Button) alert.findViewById(R.id.start_over_btn);
        startOverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                if (startOverClickListener != null)
                    startOverClickListener.onClick(v);

                alert.dismiss();

            }
        });
        Button retryBtn = (Button) alert.findViewById(R.id.retry_btn);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                if (retryClickListener != null)
                    retryClickListener.onClick(v, editText.getText().toString());

                alert.dismiss();

            }
        });
        return alert;
    }

    public static AlertDialog showPhoneNumberAlertDialog(final Context context, final String screen) {
        if (!Utils.canDial(context)) {
            return AlertUtils.showGenericAlert(context, context.getString(R.string.device_cant_make_call));
        }

        GoogleAnalyticsHelper.trackEvent(CATEGORY_EMERGENCY_OPTIONS, CALL_PRESS, screen);
        LayoutInflater inflater = LayoutInflater.from(context);

        AlertDialogEmergencyNumbersBinding binding = AlertDialogEmergencyNumbersBinding.inflate(inflater);

        final AlertDialog alert = buildAlert(context, binding.getRoot(), true);
        alert.show();

        binding.addMoreNumbersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent i = new Intent(context, SettingsFragmentStackActivity.class);
                i.setAction(SettingsFragmentStackActivity.location_emergency_contact);
                context.startActivity(i);
                if (context instanceof Activity)
                    ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        final Map<EmergencyContact.ContactType, EmergencyContact> map = EmergencyContactDatabaseService.getEmergencyContacts(UserUtils.getLastViewedLocationId());

        if (Utils.isDemo() || map.containsKey(EmergencyContact.ContactType.police) &&
                !StringUtils.isNullOrEmpty(map.get(EmergencyContact.ContactType.police).phoneNumber)) {
            binding.callPoliceBtn.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alert.dismiss();

                            if (Utils.isDemo()) {
                                AlertUtils.showGenericAlert(context, context.getString(R.string.take_action), context.getString(R.string.take_action_dsc));
                                return;
                            }

                            String policeNumber = map.get(EmergencyContact.ContactType.police).phoneNumber;
                            showAlertForNumber(context, POLICE, policeNumber, screen);

                        }
                    });
        } else {
            binding.addMoreNumbersBtn.setVisibility(View.VISIBLE);
            binding.callPoliceBtn.setVisibility(View.GONE);
        }
        if (Utils.isDemo() || map.containsKey(EmergencyContact.ContactType.ems) &&
                !StringUtils.isNullOrEmpty(map.get(EmergencyContact.ContactType.ems).phoneNumber)) {
            binding.callMedicalBtn.setOnClickListener(

                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();


                            if (Utils.isDemo()) {
                                AlertUtils.showGenericAlert(context, context.getString(R.string.take_action), context.getString(R.string.take_action_dsc));
                                return;
                            }
                            String medicalNumber = map.get(EmergencyContact.ContactType.ems).phoneNumber;
                            showAlertForNumber(context, MEDICAL, medicalNumber, screen);
                        }
                    });
        } else {
            binding.addMoreNumbersBtn.setVisibility(View.VISIBLE);
            binding.callMedicalBtn.setVisibility(View.GONE);
        }

        if (Utils.isDemo() || map.containsKey(EmergencyContact.ContactType.fire) &&
                !StringUtils.isNullOrEmpty(map.get(EmergencyContact.ContactType.fire).phoneNumber)) {
            binding.callFireBtn.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();

                            if (Utils.isDemo()) {
                                AlertUtils.showGenericAlert(context, context.getString(R.string.take_action), context.getString(R.string.take_action_dsc));
                                return;
                            }

                            String fireNumber = map.get(EmergencyContact.ContactType.fire).phoneNumber;
                            showAlertForNumber(context, FIRE, fireNumber, screen);
                        }
                    });

        } else {
            binding.addMoreNumbersBtn.setVisibility(View.VISIBLE);
            binding.callFireBtn.setVisibility(View.GONE);
        }
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_EMERGENCY_OPTIONS, ACTION_CALL_CANCEL, screen);
                alert.dismiss();
            }
        });

        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_EMERGENCY_OPTIONS, ACTION_CALL_CANCEL, screen);

            }
        });


        Window window = alert.getWindow();
        if (window != null) {
            WindowManager.LayoutParams wlp = window.getAttributes();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            window.setAttributes(wlp);
        }
        return alert;
    }

    private static final int POLICE = 1;
    private static final int FIRE = 2;
    private static final int MEDICAL = 3;

    public static void showAlertForNumber(final Context context, int type, final String number, final String screen) {
        if (context == null)
            return;

        int buttonTextId;
        int messageTextId;

        final String callType;

        switch (type) {
            case POLICE:
                messageTextId = R.string.sure_call_police;
                buttonTextId = R.string.yes_call_police;
                callType = ACTION_CALL_POLICE;
                break;
            case FIRE:
                messageTextId = R.string.sure_call_fire;
                buttonTextId = R.string.yes_call_fire;
                callType = ACTION_CALL_EMS;
                break;
            case MEDICAL:
                messageTextId = R.string.sure_call_ems;
                buttonTextId = R.string.yes_call_ems;
                callType = ACTION_CALL_FIRE;
                break;
            default:
                return;
        }

        AlertDialog alertDialog = AlertUtils.showGenericAlert(context, context.getString(messageTextId), null, 0, context.getString(buttonTextId), null, 0, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_EMERGENCY_OPTIONS, callType, screen);
                Intent intent = new Intent(
                        Intent.ACTION_DIAL, Uri.fromParts(
                        "tel", number, null));
                context.startActivity(intent);
            }
        }, null);

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                GoogleAnalyticsHelper.trackEvent(CATEGORY_EMERGENCY_OPTIONS, ACTION_CALL_CANCEL, screen);

            }
        });
    }

    public static AlertDialog showDeviceUpdateFailedDialog(final Context context, final View.OnClickListener retryListener) {
        if (context == null) {
            return null;
        }

        return showGenericAlert(context, context.getString(R.string.somethings_wrong), context.getString(R.string.your_newest_canary_failed),
                R.drawable.failed_icon, context.getString(R.string.get_help), context.getString(R.string.retry_setup), 0, 0,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = context.getString(R.string.connectivity_url);
                        ZendeskUtil.loadHelpCenter(context, url);

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (retryListener != null) {
                            retryListener.onClick(v);
                        }
                    }
                }
        );

    }

    public static void showDeleteMemberAlert(final Context context, String name, final View.OnClickListener deleteListener) {
        if (context == null) {
            return;
        }
        String alertText = context.getString(R.string.delete_from_this_location, name);
        showGenericAlert(context, alertText, context.getString(R.string.to_add_again_send_invite), 0,
                context.getString(R.string.delete), context.getString(R.string.cancel), context.getResources().getColor(R.color.light_red), 0, deleteListener, null);
    }

    public static void showDeleteInvitationAlert(final Context context, String name, final View.OnClickListener deleteListener) {

        String alertText = context.getString(R.string.cancel_invitation, name);
        String detail = context.getString(R.string.to_add_again_send_invite);
        String delete = context.getString(R.string.delete);
        String cancel = context.getString(R.string.cancel);

        int leftColor = context.getResources().getColor(R.color.light_red);

        showGenericAlert(context, alertText, detail, 0, delete, cancel, leftColor, 0, deleteListener, null);

    }


    public static AlertDialog showDeleteEntryAlert(Context context, final View.OnClickListener deleteListener, final View.OnClickListener cancelListener) {
        if (context == null)
            return null;

        return showGenericAlert(context, context.getString(R.string.delete_activity), context.getString(R.string.delete_entry_dsc),
                R.drawable.trash_icon_red, context.getString(R.string.delete), context.getString(R.string.cancel),
                context.getResources().getColor(R.color.light_red), 0, deleteListener, cancelListener);
    }

    public static AlertDialog showGenericAlert(Context context, String header, View.OnClickListener onClickListener) {
        if (context == null)
            return null;
        return showGenericAlert(context, header, null, 0, context.getString(R.string.okay), null, 0, 0, onClickListener, null);
    }

    public static AlertDialog showGenericAlert(Context context, String header) {
        if (context == null)
            return null;
        return showGenericAlert(context, header, null, 0, context.getString(R.string.okay), null, 0, 0, null, null);
    }


    public static AlertDialog showGenericAlert(Context context, String header, String dsc) {
        if (context == null)
            return null;
        return showGenericAlert(context, header, dsc, 0, context.getString(R.string.okay), null, 0, 0, null, null);
    }

    public static AlertDialog showGenericAlert(Context context, String header, String dsc, View.OnClickListener onClickListener) {
        if (context == null)
            return null;
        return showGenericAlert(context, header, dsc, 0, context.getString(R.string.okay), null, 0, 0, onClickListener, null);
    }

    public static AlertDialog showGenericAlert(Context context, String header, String des, int imageRes, String leftButtonText, String rightButtonText,
                                               int leftTextColor, int rightTextColor, final View.OnClickListener leftListener, final View.OnClickListener rightListener) {

        if (context == null)
            return null;

        if ("".equalsIgnoreCase(header))
            return null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View alertView = inflater.inflate(R.layout.alert_dialog_generic, null);

        if (!Utils.isDemo()) {

            View view = alertView.findViewById(R.id.demo_tip_layout);
            view.setVisibility(View.GONE);
        }

        ImageView imageView = (ImageView) alertView.findViewById(R.id.alert_icon);
        FrameLayout imageFrameLayout = (FrameLayout) alertView.findViewById(R.id.icon_frame_layout);

        if (imageRes != 0)
            imageView.setImageResource(imageRes);
        else
            imageFrameLayout.setVisibility(View.GONE);

        TextView titleTextView = (TextView) alertView.findViewById(R.id.alert_header);
        if (header != null)
            titleTextView.setText(header);
        else
            titleTextView.setVisibility(View.GONE);

        TextView descTextView = (TextView) alertView.findViewById(R.id.alert_desc);
        if (des != null)
            descTextView.setText(des);
        else
            descTextView.setVisibility(View.GONE);

        Button leftBtn = (Button) alertView.findViewById(R.id.alert_button_left);

        Button rightBtn = (Button) alertView.findViewById(R.id.alert_button_right);


        if (rightButtonText != null)
            rightBtn.setText(rightButtonText);
        else
            rightBtn.setVisibility(View.GONE);

        leftBtn.setText(leftButtonText);

        if (leftTextColor != 0)
            leftBtn.setTextColor(leftTextColor);

        if (rightTextColor != 0)
            rightBtn.setTextColor(rightTextColor);

        final AlertDialog alert = buildAlert(context, alertView, true);

        alert.show();
        leftBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        if (leftListener != null) {
                            leftListener.onClick(v);
                        }
                    }
                });

        rightBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        if (rightListener != null) {
                            rightListener.onClick(v);
                        }

                    }
                });


        return alert;
    }

    public static AlertDialog showBleAlert(Context context, String dsc, View.OnClickListener listener) {
        if (context == null)
            return null;

        String title = context.getString(R.string.your_phone_is_not_bluetooth_capable);
        String leftButton = context.getString(R.string.get_help);
        String rightButton = context.getString(R.string.okay);

        return AlertUtils.showGenericAlert(context, title, dsc, 0, leftButton, rightButton, 0, 0, listener, null);
    }

    public static AlertDialog showWatchLiveLimitReachedDialog(Context context, View.OnClickListener listener) {
        if (context == null)
            return null;
        AlertDialog dialog = showGenericAlert(context, context.getString(R.string.watch_live_limit), null, 0, context.getString(R.string.watch_live_limit_button), null, 0, 0, listener, null);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    public static AlertDialog initLoadingDialog(Context context, String message, boolean cancelable) {
        if (context == null)
            return null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View alertView = inflater.inflate(R.layout.dialog_loading, null);

        TextView messageView = (TextView) alertView.findViewById(R.id.loading_message);
        messageView.setText(message);

        AlertDialog progressDialog = new AlertDialog.Builder(context).create();
        progressDialog.setView(alertView);
        progressDialog.setCancelable(cancelable);
        progressDialog.setCanceledOnTouchOutside(cancelable);
        int width = (int) context.getResources().getDimension(R.dimen.loading_dialog_width);
        progressDialog.show();
        progressDialog.getWindow().setLayout(width, WRAP_CONTENT);

        return progressDialog;
    }

    public static AlertDialog initLoadingDialog(Context context, String message) {
        return initLoadingDialog(context, message, true);
    }

    public static AlertDialog initSuccessDialog(Context context, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View alertView = inflater.inflate(R.layout.dialog_success, null);

        if (message != null) {
            TextView textView = (TextView) alertView.findViewById(R.id.loading_message);
            textView.setText(message);
        }

        AlertDialog progressDialog = new AlertDialog.Builder(context).create();
        progressDialog.setView(alertView);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        int width = (int) context.getResources().getDimension(R.dimen.loading_dialog_width);
        progressDialog.show();

        progressDialog.getWindow().setLayout(width, WRAP_CONTENT);

        return progressDialog;
    }

    public static AlertDialog showRemoveDevicePrompt(final Context context, String header, String body, boolean incorrectPassword, final DeviceActivationDialogListener leftListener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View alertView = inflater.inflate(R.layout.alert_dialog_remove_device, null);

        TextView titleTextView = (TextView) alertView.findViewById(R.id.alert_header);
        titleTextView.setText(header);

        TextView descTextView = (TextView) alertView.findViewById(R.id.alert_desc);
        descTextView.setText(body);

        Button rightBtn = (Button) alertView.findViewById(R.id.alert_button_right);
        rightBtn.setText(context.getString(R.string.cancel));

        final Button leftBtn = (Button) alertView.findViewById(R.id.alert_button_left);
        leftBtn.setText(context.getString(R.string.remove_device_left_button));
        leftBtn.setEnabled(false);

        final EditText userPassword = (EditText) alertView.findViewById(R.id.password);
        userPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                leftBtn.setEnabled(s.length() > 0);
            }
        });

        showSoftKeyboard(context);

        ImageButton forgotPassword = (ImageButton) alertView.findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(context, v);
                leftListener.resetPassword();
            }
        });

        final AlertDialog alert = buildAlert(context, alertView, true);

        leftBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideSoftKeyboard(context, v);
                        if (leftListener != null) {
                            leftListener.onSubmit(userPassword.getText().toString());
                        }
                    }
                });

        rightBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideSoftKeyboard(context, v);
                        alert.dismiss();
                    }
                });

        alert.show();
        return alert;
    }

    public static AlertDialog showResetPasswordSuccessDialog(final Context context, final View.OnClickListener listener) {
        return showGenericAlert(context, context.getString(R.string.reset_password_follow_up), listener);
    }

    public static AlertDialog showCannotDeactivateDeviceDialog(Context context, View.OnClickListener onClickListener) {
        return showGenericAlert(context,
                context.getString(R.string.device_cant_remove_header),
                context.getString(R.string.device_cant_remove_body),
                0,
                context.getString(R.string.cancel),
                context.getString(R.string.device_cant_remove_tap),
                0,
                0,
                null,
                onClickListener);
    }

    private static void showSoftKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static AlertDialog showSuccessDialog(Context context) {
        return initSuccessDialog(context, null);
    }

    public static AlertDialog showSuccessDialog(Context context, String message) {
        return initSuccessDialog(context, message);
    }

    public static AlertDialog buildAlert(Context context, View bodyView, boolean isCancellable) {
        AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setView(bodyView, 0, 0, 0, 0);
        alert.setCancelable(isCancellable);
        alert.setCanceledOnTouchOutside(isCancellable);
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = alert.getWindow();
        Point size = new Point();
        window.getWindowManager().getDefaultDisplay().getSize(size);

        int maxWidth = DensityUtil.dip2px(context, 295);
        int orientation = context.getResources().getConfiguration().orientation;
        int width;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            width = size.x - DensityUtil.dip2px(context, 50);
            if (width > maxWidth) {
                width = maxWidth;
            }
            Log.i(LOG_TAG, "width " + DensityUtil.px2dip(context, width) + " config " + context.getResources().getConfiguration().orientation);
        } else {
            width = size.y - DensityUtil.dip2px(context, 50);
            if (width > maxWidth) {
                width = maxWidth;
            }
            Log.i(LOG_TAG, "width " + DensityUtil.px2dip(context, width) + " config " + context.getResources().getConfiguration().orientation);
        }
        alert.show();
        alert.getWindow().setLayout(width, WRAP_CONTENT);

        return alert;
    }

    public static AlertDialog showMemberDialog(@NonNull final BaseActivity baseActivity,
                                               @NonNull final View.OnClickListener onClickListener) {

        // mis-clicking prevention, using threshold of 1000 ms
        if (TouchTimeUtil.dontAllowTouch())
            return null;

        LayoutInflater inflater = LayoutInflater.from(baseActivity);
        AlertDialogAddPersonBinding alertView = AlertDialogAddPersonBinding.inflate(inflater);

        final AlertDialog alertDialog = buildAlert(baseActivity, alertView.getRoot(), true);

        alertView.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(baseActivity, SettingsFragmentStackActivity.class);
                intent.setAction(invite_members);
                baseActivity.startActivity(intent);
                baseActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                PreferencesUtils.setHasGoneToMembers(true);
            }
        });

        alertView.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                onClickListener.onClick(v);
            }
        });

        return alertDialog;
    }

}
