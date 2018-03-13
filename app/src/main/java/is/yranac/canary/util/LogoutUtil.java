package is.yranac.canary.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.urbanairship.UAirship;

import org.json.JSONException;

import is.yranac.canary.R;
import is.yranac.canary.messages.PurgeDatabase;
import is.yranac.canary.model.customer.CurrentCustomer;
import is.yranac.canary.services.api.DeviceTokenAPIServices;
import is.yranac.canary.services.geofence.SetUpGeofence;
import is.yranac.canary.services.intent.GlobalAPIntentServiceManager;
import is.yranac.canary.ui.SetupFragmentStackActivity;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Schroeder on 11/30/15.
 */
public class LogoutUtil {


    public static void logout(final Context context) {

        if (context instanceof Activity) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.logout)
                    .setMessage(R.string.are_you_sure_sign_out)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            continueLogout(context, true);
                        }

                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
            return;
        }
        continueLogout(context, true);
    }

    public static void logoutSilent(Context context) {
        continueLogout(context, true);
    }

    public static void continueLogout(Context context) {
        continueLogout(context, false);
    }


    public static void continueLogout(final Context context, boolean wait) {

        if (wait) {
            final AlertDialog mDialog = AlertUtils.initLoadingDialog(context, context.getString(R.string.logout));

            mDialog.show();
            String apid = UAirship.shared().getPushManager().getChannelId();
            if (apid == null){
                finishLogout(context);
            }
            DeviceTokenAPIServices.deactivateDeviceToken(apid, new Callback<Void>() {
                @Override
                public void success(Void aVoid, Response response) {

                    finishLogout(context);
                    mDialog.dismiss();
                    GoogleAnalyticsHelper.removeUserId();
                }

                @Override
                public void failure(RetrofitError error) {
                    mDialog.dismiss();
                    Response response = error.getResponse();
                    if (response != null) {
                        if (response.getStatus() == 404 || response.getStatus() == 401) {

                            finishLogout(context);
                            return;
                        }
                    }
                    try {
                        if (context instanceof Activity) {
                            AlertUtils.showGenericAlert(context, Utils.getErrorMessageFromRetrofit(context, error));
                        }
                    } catch (JSONException ignore) {

                    }
                }
            });
        } else {
            finishLogout(context);
            String apid = UAirship.shared().getPushManager().getChannelId();

            DeviceTokenAPIServices.deactivateDeviceToken(apid, new Callback<Void>() {
                @Override
                public void success(Void aVoid, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }


    }

    private static void finishLogout(Context context) {
        PreferencesUtils.removeUser();
        CurrentCustomer.clearCache();
        SetUpGeofence.stopService(context.getApplicationContext());
        PreferencesUtils.removePassCode();
        GlobalAPIntentServiceManager.cancelAlarms(context);
        UserUtils.setLastViewedLocationId(-1);
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
        Intent i = new Intent(context, SetupFragmentStackActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        TinyMessageBus.post(new PurgeDatabase());
    }

}
