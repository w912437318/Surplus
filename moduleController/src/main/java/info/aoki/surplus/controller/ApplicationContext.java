package info.aoki.surplus.controller;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

import info.aoki.surplus.system.ApplicationConfig;
import info.aoki.surplus.system.GreenDaoUtil;
import info.aoki.surplus.system.LogUtil;
import info.aoki.surplus.system.NotificationUtil;
import info.aoki.surplus.system.ThreadPoolUtil;

/**
 * <h3>Surplus Application</h3>
 */
public final class ApplicationContext extends Application {
    private final String TAG = ApplicationContext.class.getName();
    /**
     * Thread Pool Util
     * <p>Don't create any thread by hand, You can create {@link Runnable} command</p>
     * <p>And thread pool will execute your command</p>
     */
    private ThreadPoolUtil mThreadPoolUtil;
    /**
     * All application configuration will set in this on application launch
     */
    private ApplicationConfig mApplicationConfig;
    /**
     * Databases Util
     */
    private GreenDaoUtil mGreenDaoUtil;
    /**
     * Notification Util
     * <p>Any notification should send by this class</p>
     */
    private NotificationUtil mNotificationUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        // Application launch, start init application
        initApplication();
    }

    /**
     * Init application
     * <p>Init Flow:</p>
     * <ol>
     * <li>Create Thread</li>
     * <li>Load App Config</li>
     * <li>Create GreenDaoUtil -- Database Util</li>
     * <li>Create Notification Util</li>
     * </ol>
     */
    private void initApplication() {
        LogUtil.d(TAG, "Application init started.");

        // Thread pool util created.
        LogUtil.d(TAG, "Thread pool created.");
        ThreadPoolUtil.createThreadPoolUtil();
        this.mThreadPoolUtil = ThreadPoolUtil.getThreadPoolUtil();

        // Application Config created.
        LogUtil.d(TAG, "Application config created.");
        ApplicationConfig.createApplicationConfig(this);
        this.mApplicationConfig = ApplicationConfig.getConfig();

        // GreenDaoUtil created.
        LogUtil.d(TAG, "GreenDao util created.");
        GreenDaoUtil.createDaoUtil(this);
        this.mGreenDaoUtil = GreenDaoUtil.getGreenDaoUtil();

        // Notification util created.
        LogUtil.d(TAG, "Notification util created.");
        NotificationUtil.createNotificationUtil(this);
        this.mNotificationUtil = NotificationUtil.getInstance();

        // Init router
        ARouter.openLog();
        ARouter.openDebug();
        ARouter.init(this);

        LogUtil.d(TAG, "Application init completed.");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtil.w(TAG, "Low Memory!");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LogUtil.d(TAG, "Application (Surplus) terminated!");
        // Close database
        this.mGreenDaoUtil.close();

        // Release resource
        this.mThreadPoolUtil.shutdown(); // Close thread pool
        this.mThreadPoolUtil = null;
        this.mApplicationConfig = null;
        this.mGreenDaoUtil = null;
        this.mNotificationUtil = null;

    }
}
