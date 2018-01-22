package merlin.yx.com.detecter;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

/**
 * Author       : yizhihao (Merlin)
 * Create time  : 2018-01-21 10:03
 * contact      :
 * 562536056@qq.com || yizhihao.hut@gmail.com
 */
public class LogMonitor {
    private static LogMonitor sInstance = new LogMonitor();
    private HandlerThread mLogThread = new HandlerThread("log");
    private Handler mIoHandler;
    private static final long TIME_BLOCK = 1000L;

    private LogMonitor() {
        mLogThread.start();
        mIoHandler = new Handler(mLogThread.getLooper());
    }

    private static Runnable mLogRunnable = new Runnable() {
        @Override
        public void run() {
            StringBuilder sb = new StringBuilder();
            StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
            for (StackTraceElement s : stackTrace) {
                sb.append(s.toString() + "\n");
            }
            Log.e("TAG", sb.toString());
        }
    };

    public static LogMonitor getInstance() {
        return sInstance;
    }

    public boolean isMonitor() {
        return mIoHandler.hasMessages(mLogRunnable);
    }


    public void startMonitor() {
        mIoHandler.postDelayed(mLogRunnable, TIME_BLOCK);
    }

    public void removeMonitor() {
        mIoHandler.removeCallbacks(mLogRunnable);
    }
}
