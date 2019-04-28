package info.aoki.surplus.system;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadScheduler {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE_TIME_SECONDS = 30;
    private static Handler sHandler;
    private static ThreadScheduler sSupport;
    private ThreadPoolExecutor mExecutor;

    private ThreadScheduler() {
        // 线程池创建
        mExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME_SECONDS,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(64));
        // 线程切换 Handler
        // 为保证回调方法能够顺利回到主线程，需要确保执行者在主线程创建
        sHandler = new InternalHandler(Looper.getMainLooper());
    }

    /**
     * <h3>初始化线程池</h3>
     * <p>建议在 Application 类中调用 <code>方法</code>，以此确保内部线程池在主线程中创建</p>
     * <p>确保初始化方法在应用程序中只被调用了一次</p>
     * <p>在应用程序关闭时，或不再需要使用此线程池时调用 {@link ThreadScheduler#shutdown()} 方法关闭线程池/p>
     */
    public static void initExecutor() {
        if (sSupport != null)
            throw new IllegalStateException(
                    "ThreadSupport is already initialized, Don't initialize again.");

        synchronized (ThreadScheduler.class) {
            if (sSupport == null) {
                sSupport = new ThreadScheduler();
            }
        }
    }

    /**
     * <h3>获取线程池执行者对象</h3>
     * <p>在调用此方法获取执行者对象之前，请确保已经初始化执行者</p>
     *
     * @return ThreadSupport 对象
     */
    public static ThreadScheduler getExecutor() {
        if (sSupport == null)
            throw new IllegalStateException(
                    "ThreadSupport didn't initialize, Please initialize in Application class.");

        return sSupport;
    }

    /**
     * <h3>执行任务队列</h3>
     * <p>执行者会在子线程中一次执行传入的任务，并在所有任务执行完成之后执行回调，以此通知调用者任务队列以及执行完成</p>
     * <p></p>
     *
     * @param finishCallBack 任务完成之后执行的回调
     * @param commands       任务队列
     */
    public void execute(@Nullable final OnTaskFinishCallBack finishCallBack,
                        final Runnable... commands) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Message callBackMsg;
                try {
                    for (Runnable command : commands) {
                        command.run();
                    }
                } catch (Exception e) {
                    if (finishCallBack != null) {
                        callBackMsg = Message.obtain(sHandler, InternalHandler.FAILED_WHAT_CODE);
                        callBackMsg.obj = finishCallBack;
                        sHandler.sendMessage(callBackMsg);
                        return;
                    }
                    e.printStackTrace();
                }
                if (finishCallBack != null) {
                    callBackMsg = Message.obtain(sHandler, InternalHandler.FINISH_WHAT_CODE);
                    callBackMsg.obj = finishCallBack;
                    sHandler.sendMessage(callBackMsg);
                }
            }
        });
    }

    /**
     * <h3>执行任务</h3>
     * <p>使用此方式执行任务时，任务会被静默执行。即任务完成后调用者无法从执行者处获得任何反馈</p>
     * <p>此行此任务时发生的异常会被默认捕捉，防止其影响程序的正常运行</p>
     * <p>建议使用此方法执行一些非必要的耗时操作</p>
     *
     * @param command 任务
     */
    public void execute(Runnable command) {
        try {
            mExecutor.execute(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <h3>关闭核心线程池</h3>
     * <p>请在应用程序关闭时或者其他适当的时候调用此方法关闭核心线程池</p>
     * <strong>请勿重复调用此方法</strong>
     */
    public void shutdown() {
        if (mExecutor.isTerminating() || mExecutor.isShutdown() || mExecutor.isTerminated()) return;
        mExecutor.shutdown();
    }

    private static class InternalHandler extends Handler {

        static final int FAILED_WHAT_CODE = 0;
        static final int FINISH_WHAT_CODE = 1;

        InternalHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FAILED_WHAT_CODE:
                    ((OnTaskFinishCallBack) msg.obj).onFailed();
                    break;
                case FINISH_WHAT_CODE:
                    ((OnTaskFinishCallBack) msg.obj).onFinish();
                    break;
                default:
                    break;
            }
        }
    }

    public interface OnTaskFinishCallBack {
        void onFinish();

        void onFailed();
    }
}
