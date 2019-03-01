package info.aoki.surplus.system;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * <h3></h3>
 * <p>Create time:12/3/18 5:13 PM</p>
 *
 * @author wong
 * @version 1.0
 */
public class DriverUtil {

    /**
     * Get driver IMEI code.
     * <strong>Throw NullPointException, If doesn't has {@link Manifest.permission#READ_PHONE_STATE} permission</strong>
     * <p>Use {@link PermissionUtil} request {@link Manifest.permission#READ_PHONE_STATE} permission first</p>
     *
     * @return String Driver ID(IMEI)
     */
    @SuppressLint("HardwareIds")
    public static String getDriverIMEI() {
        // Get telephone manger
        TelephonyManager telephonyManager =
                (TelephonyManager) ApplicationConfig.getConfig().getContext().
                        getSystemService(Context.TELEPHONY_SERVICE);
        // Check permission
        if (PermissionUtil.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)) {
            assert telephonyManager != null;
            return telephonyManager.getDeviceId();
        }
        // Doesn't has permission
        throw new NullPointerException("No permission:READ_PHONE_STATE, Can't read driver IMEI code.");
    }
}
