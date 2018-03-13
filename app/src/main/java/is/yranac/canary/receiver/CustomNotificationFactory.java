package is.yranac.canary.receiver;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.urbanairship.push.PushMessage;
import com.urbanairship.push.notifications.NotificationFactory;

/**
 * Created by michaelschroeder on 10/31/16.
 */
public class CustomNotificationFactory extends NotificationFactory {

    public CustomNotificationFactory(Context context) {
        super(context);
    }

    @Override
    public NotificationCompat.Builder createNotificationBuilder(@NonNull PushMessage message, int notificationId, @Nullable NotificationCompat.Style defaultStyle) {

        return null;
    }

}
