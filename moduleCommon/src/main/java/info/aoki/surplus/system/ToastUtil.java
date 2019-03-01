package info.aoki.surplus.system;

import android.annotation.SuppressLint;
import android.widget.Toast;

/**
 * <h3>ToastUtil</h3>
 * <p>Create time:11/30/18 9:16 PM</p>
 *
 * @author wong
 * @version 1.0
 */
public class ToastUtil {
    private static Toast sToast;

    /**
     * <p>Show toast to user</p>
     *
     * @param message String
     */
    @SuppressLint("ShowToast")
    public static void showToast(String message) {
        if (sToast == null) {
            sToast = Toast.makeText(ApplicationConfig.getConfig().getContext(), message, Toast.LENGTH_SHORT);
        }
        sToast.setText(message);
        sToast.show();
    }

    /**
     * <p>Show toast to user</p>
     *
     * @param resId String ID
     */
    @SuppressLint("ShowToast")
    public static void showToast(int resId) {
        if (sToast == null) {
            sToast = Toast.makeText(ApplicationConfig.getConfig().getContext(), resId, Toast.LENGTH_SHORT);
        }
        sToast.setText(resId);
        sToast.show();
    }
}
