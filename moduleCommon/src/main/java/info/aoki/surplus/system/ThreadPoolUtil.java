package info.aoki.surplus.system;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <h3>ThreadPoolUtil</h3>
 * <p>Create time:12/4/18 7:29 PM</p>
 *
 * @author wong
 * @version 1.0
 */
public class ThreadPoolUtil {
    private static ThreadPoolUtil sThreadPoolUtil;
    private ThreadPoolExecutor mThreadPoolExecutor;

    private ThreadPoolUtil() {
        /*
         Create Thread Pool
         Max thread size 10
         Core thread 5
         Keep alive time 3 minutes
          */
        mThreadPoolExecutor = new ThreadPoolExecutor(
                5,
                10,
                3,
                TimeUnit.MINUTES,
                new LinkedBlockingDeque<>());
    }

    /**
     * Execute runnable
     *
     * @param command {@link Runnable}
     */
    public void execute(Runnable command) {
        mThreadPoolExecutor.execute(command);
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return this.mThreadPoolExecutor;
    }

    /**
     * Create Thread Pool
     */
    public static void createThreadPoolUtil() {
        if (sThreadPoolUtil == null) {
            synchronized (ThreadPoolUtil.class) {
                if (sThreadPoolUtil == null) {
                    sThreadPoolUtil = new ThreadPoolUtil();
                }
            }
        }
    }

    public static ThreadPoolUtil getThreadPoolUtil() {
        if (sThreadPoolUtil == null)
            throw new NullPointerException("You didn't call createThreadPoolUtil() in ApplicationContext");
        return sThreadPoolUtil;
    }

    public void shutdown() {
        mThreadPoolExecutor.shutdown();
    }
}