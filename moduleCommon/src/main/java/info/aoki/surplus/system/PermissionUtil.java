package info.aoki.surplus.system;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * <h3>PermissionUtil</h3>
 * <p>Create time:12/3/18 7:04 PM</p>
 * <p>Permission request framework</p>
 * <h4>Request 3 Step:</h4>
 * <ol>
 * <li>Check Permission</li>
 * <li>Check has user refuse the Permission already</li>
 * <li>If don't have the Permission, and it hasn't be refuse, then request it</li>
 * </ol>
 *
 * @author wong
 * @version 1.0
 */
public class PermissionUtil {
    public final static int REQUEST_CODE_REQUEST_PERMISSION = 200;

    public static boolean checkSelfPermission(String permission) {
        int hasPermission = ContextCompat.checkSelfPermission(
                ApplicationConfig.getConfig().getContext(),
                permission);
        return hasPermission == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean shouldRequestPermission(Activity activity, String permission) {
        return !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    public static void requestPermission(Activity activity, String permission) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_CODE_REQUEST_PERMISSION);
    }
}
