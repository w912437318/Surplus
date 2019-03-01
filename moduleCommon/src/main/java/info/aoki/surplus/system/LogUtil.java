package info.aoki.surplus.system;

import android.util.Log;

/**
 * <h3>LogUtil</h3>
 * <p>Create time:12/3/18 3:49 PM</p>
 * <p>Attention: All log should print by this class</p>
 *
 * @author wong
 * @version 1.0
 */
public class LogUtil {
    /**
     * Debug switch
     * <p>
     *     if true : print log what level higher than debug<br/>
     *     if false : don't print log, else info level
     * </p>
     */
    private final static boolean DEBUG_MODE = true;

    public static void i(String TAG, String message) {
        Log.i(TAG, message);
    }

    public static void d(String TAG, String message) {
        if (!DEBUG_MODE) return;
        Log.d(TAG, message);
    }

    public static void w(String TAG, String message) {
        if (!DEBUG_MODE) return;
        Log.w(TAG, message);
    }

    public static void e(String TAG, String message) {
        if (!DEBUG_MODE) return;
        Log.e(TAG, message);
    }
}
