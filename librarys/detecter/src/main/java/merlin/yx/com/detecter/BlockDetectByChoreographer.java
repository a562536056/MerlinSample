package merlin.yx.com.detecter;

import android.view.Choreographer;

import java.util.concurrent.TimeUnit;

/**
 * Author       : yizhihao (Merlin)
 * Create time  : 2018-01-21 10:03
 * contact      :
 * 562536056@qq.com || yizhihao.hut@gmail.com
 */
public class BlockDetectByChoreographer {

    public static void start() {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            long lastFrameTimeNanos = 0;
            long currentFrameTimeNanos = 0;

            @Override
            public void doFrame(long frameTimeNanos) {
                if(lastFrameTimeNanos == 0){
                    lastFrameTimeNanos = frameTimeNanos;
                }
                currentFrameTimeNanos = frameTimeNanos;
                long diffMs = TimeUnit.MILLISECONDS.convert(currentFrameTimeNanos-lastFrameTimeNanos, TimeUnit.NANOSECONDS);
                if (diffMs > 16.6f) {
                    long droppedCount = (long) (diffMs / 16.6);
                }
                if (LogMonitor.getInstance().isMonitor()) {
                    LogMonitor.getInstance().removeMonitor();
                }
                LogMonitor.getInstance().startMonitor();
                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }

}
