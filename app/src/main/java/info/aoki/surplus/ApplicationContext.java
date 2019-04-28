package info.aoki.surplus;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;

import info.aoki.surplus.system.GreenDaoUtil;
import info.aoki.surplus.system.ThreadScheduler;

/**
 * <h3>Surplus Application</h3>
 */
public final class ApplicationContext extends Application {
    private final String TAG = ApplicationContext.class.getName();
    private GreenDaoUtil mGreenDaoUtil;
    private ThreadScheduler mThreadSupport;

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
    }

    private void initApplication() {
        initThreadScheduler();
        initGreenDaoUtil();
        initARouter();
    }

    private void initThreadScheduler() {
        ThreadScheduler.initExecutor();
        mThreadSupport = ThreadScheduler.getExecutor();
    }

    private void initGreenDaoUtil() {
        GreenDaoUtil.createDaoUtil(this);
        mGreenDaoUtil = GreenDaoUtil.getGreenDaoUtil();
    }

    private void initARouter() {
        ARouter.openDebug();
        ARouter.openLog();
        ARouter.init(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mGreenDaoUtil.close();
        mThreadSupport.shutdown();
    }
}
