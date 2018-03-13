package is.yranac.canary.retrofit;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import javax.net.ssl.SSLHandshakeException;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.R;
import is.yranac.canary.messages.InvalidCert;
import is.yranac.canary.messages.LoggedOut;
import is.yranac.canary.services.database.BaseDatabaseService;
import is.yranac.canary.services.intent.GlobalAPIntentServiceManager;
import is.yranac.canary.ui.SetupFragmentStackActivity;
import is.yranac.canary.util.MyLifecycleHandler;
import is.yranac.canary.util.PreferencesUtils;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.keystore.KeyStoreHelper;
import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

public class RetrofitErrorHandler implements ErrorHandler {
    public static final String TAG = "RetrofitErrorHandler";
    private static final int notiicationId = 0x345345;

    @Override
    public Throwable handleError(RetrofitError error) {

        handleRetrofitError(error);
        return error;
    }

    public static void handleRetrofitError(RetrofitError error) {
        Response response = error.getResponse();

        if (error.getCause() instanceof SSLHandshakeException) {
            TinyMessageBus.post(new InvalidCert());
            return;
        }
        if (response != null) {
            final int status = response.getStatus();

            if (status == HTTP_UNAUTHORIZED) {

                if (KeyStoreHelper.hasGoodRefreshToken())
                    return;


                PreferencesUtils.removeUser();
                BaseDatabaseService.purgeDatabase();
                Context context = CanaryApplication.getContext();

                GlobalAPIntentServiceManager.cancelAlarms(context);
                if (!MyLifecycleHandler.applicationInBackground()) {
                    TinyMessageBus.post(new LoggedOut());
                } else {
                    NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                    bigTextStyle.setBigContentTitle(context.getString(R.string.app_name));
                    bigTextStyle.bigText(context.getString(R.string.you_ve_been_logged_out));


                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(CanaryApplication.getContext())
                                    .setSmallIcon(R.drawable.notification_logo)
                                    .setStyle(bigTextStyle)
                                    .setContentText(context.getString(R.string.you_ve_been_logged_out))
                                    .setAutoCancel(true);
                    Intent resultIntent = new Intent(context, SetupFragmentStackActivity.class);
                    resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    // Adds the back stack for the Intent (but not the Intent itself)
                    stackBuilder.addParentStack(SetupFragmentStackActivity.class);

                    // Adds the Intent that starts the Activity to the top of the stack
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_ONE_SHOT
                            );
                    builder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(notiicationId, builder.build());
                }

            }
        }
    }
}
