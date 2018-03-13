package is.yranac.canary.util;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;

/**
 * Created by michaelschroeder on 4/19/17.
 */

public class PermissionUtil {

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasPermission(String[] permissions, int[] grantResults, FragmentActivity activity) {
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                String permission = permissions[i];
                boolean showRationale = activity.shouldShowRequestPermissionRationale(permission);
                if (!showRationale) {
                    AlertUtils.showPermissionOverlay(activity, permission);
                }
                return false;
            }
        }

        return true;
    }
}
