package merlin.yx.com.detecter;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Author       : yizhihao (Merlin)
 * Create time  : 2018-01-21 10:03
 * contact      :
 * 562536056@qq.com || yizhihao.hut@gmail.com
 */
public class LogMonitor {

    public static final String TAG = LogMonitor.class.getSimpleName();

    private static LogMonitor sInstance = new LogMonitor();
    private HandlerThread mLogThread = new HandlerThread("log");
    private Handler mIoHandler;
    private static final long TIME_BLOCK = 1000L;

    public static final int LOG_MSG = 0x1;

    private LogMonitor() {
        mLogThread.start();
        mIoHandler = new Handler(mLogThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case LOG_MSG:
                        StringBuilder sb = new StringBuilder();
                        StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
                        for (StackTraceElement s : stackTrace) {
                            sb.append(s.toString() + "\n");
                        }
                        Log.e(TAG, sb.toString());
                        break;
                }
            }
        };
    }

    public static LogMonitor getInstance() {
        return sInstance;
    }

    public boolean isMonitor() {
        return mIoHandler.hasMessages(LOG_MSG);
    }


    public void startMonitor() {
        mIoHandler.sendEmptyMessageDelayed(LOG_MSG, TIME_BLOCK);
    }

    public void removeMonitor() {
        mIoHandler.removeMessages(LOG_MSG);
    }
}
