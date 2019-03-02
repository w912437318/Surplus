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
    private ThreadPoolUtil mThreadPoolUtil;
    private ApplicationConfig mApplicationConfig;
    private GreenDaoUtil mGreenDaoUtil;
    private NotificationUtil mNotificationUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
    }

    private void initApplication() {
        ThreadPoolUtil.createThreadPoolUtil();
        mThreadPoolUtil = ThreadPoolUtil.getThreadPoolUtil();

        ApplicationConfig.createApplicationConfig(this);
        mApplicationConfig = ApplicationConfig.getConfig();

        GreenDaoUtil.createDaoUtil(this);
        mGreenDaoUtil = GreenDaoUtil.getGreenDaoUtil();

        LogUtil.d(TAG, "Notification util created.");
        NotificationUtil.createNotificationUtil(this);
        mNotificationUtil = NotificationUtil.getInstance();

        ARouter.openLog();
        ARouter.openDebug();
        ARouter.init(this);
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

        mGreenDaoUtil.close();

        mThreadPoolUtil.shutdown();
        mThreadPoolUtil = null;
        mApplicationConfig = null;
        mGreenDaoUtil = null;
        mNotificationUtil = null;

    }
}
