package merlin.yx.com.detecter;

import android.os.Looper;
import android.util.Log;
import android.util.Printer;

/**
 * Author       : yizhihao (Merlin)
 * Create time  : 2018-01-22 15:41
 * contact      :
 * 562536056@qq.com || yizhihao.hut@gmail.com
 */
public class BlockDetectByPrinter {

    public static final String TAG = BlockDetectByPrinter.class.getSimpleName();

    public static void start() {
        Looper.getMainLooper().setMessageLogging(new Printer() {
            private static final String START = ">>>>> Dispatching";
            private static final String END = "<<<<< Finished";
            @Override
            public void println(String x) {
                if (x.startsWith(START)) {
                    LogMonitor.getInstance().startMonitor();
                }
                Log.e(TAG,"x = " + x);
                if (x.startsWith(END)) {
                    LogMonitor.getInstance().removeMonitor();
                }
            }
        });
    }
}