package info.aoki.surplus.system;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import java.io.InputStream;

/**
 * <h3>NotificationUtil</h3>
 * <p>Create time:12/12/18 7:25 PM</p>
 *
 * @author wong
 * @version 1.0
 */
public class NotificationUtil {
    @SuppressLint("StaticFieldLeak")
    private static NotificationUtil ourInstance;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;

    public static void createNotificationUtil(Context context) {
        if (ourInstance == null) {
            synchronized (NotificationUtil.class) {
                if (ourInstance == null) {
                    ourInstance = new NotificationUtil(context);
                }
            }
        }
    }

    public static NotificationUtil getInstance() {
        if (ourInstance == null)
            throw new NullPointerException("Did you forget create NotificationUtil Instance");
        return ourInstance;
    }

    private NotificationUtil(Context context) {
        this.mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.mNotificationBuilder = new NotificationCompat.Builder(context);
    }

    public void sendNotification(int id, String title, String text, int icon) {
        this.mNotificationManager.notify(
                id,
                buildNotification(title, text, icon));
    }

    public void sendNotification(int id, String title, String text, InputStream icon) {
        this.mNotificationManager.notify(
                id,
                buildNotification(title, text, icon));
    }

    public void recallNotification(int id) {
        this.mNotificationManager.cancel(id);
    }

    private Notification buildNotification(String title, String text, int icon) {
        return this.mNotificationBuilder.
                setContentTitle(title).
                setSmallIcon(icon).
                setContentText(text).
                build();
    }

    private Notification buildNotification(String title, String text, InputStream icon) {
        return this.mNotificationBuilder.
                setContentTitle(title).
                setLargeIcon(BitmapFactory.decodeStream(icon)).
                setContentText(text).
                build();
    }
}
