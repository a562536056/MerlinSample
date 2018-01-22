package merlin.yx.com.common.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import merlin.yx.com.common.constants.GlobalConstants;
import merlin.yx.com.common.utils.DisplayUtils;


/**
 * 具有阻尼效果的ScrollView
 * Author       : yizhihao
 * Create time  : 2016-11-22 下午12:01
 * email        : 562536056@qq.com || yizhihao.hut@gmail.com
 */
public class ScrollViewExtend extends ScrollView{

    // 拖动的距离 size = 4 的意思 只允许拖动屏幕的1/4
    private static final int size = 4;
    private static final String TAG = ScrollViewExtend.class.getSimpleName();
    // scrolll speed
    private float mSpeedUp = 1.4f;
    private View inner;
    private float y;
    private Rect normal = new Rect();
    private int mOverScrollDistance;

    public ScrollViewExtend setOnScrollChangeLintener(OnScrollChangeLintener onScrollChangeLintener) {
        mOnScrollChangeLintener = onScrollChangeLintener;
        return this;
    }

    private OnScrollChangeLintener mOnScrollChangeLintener;

    public ScrollViewExtend(Context context) {
        super(context);
    }

    public ScrollViewExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
        mOverScrollDistance = DisplayUtils.dip2px(getContext(), GlobalConstants.OVER_SCROLL_MAX_DISTANCE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner == null) {
            return super.onTouchEvent(ev);
        } else {
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected boolean overScrollBy(
            int deltaX, int deltaY,
            int scrollX, int scrollY,
            int scrollRangeX, int scrollRangeY,
            int maxOverScrollX, int maxOverScrollY,
            boolean isTouchEvent) {

        return super.overScrollBy(
                deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, maxOverScrollX, mOverScrollDistance,
                isTouchEvent);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, (int) (t * mSpeedUp), oldl, oldt);
        if(mOnScrollChangeLintener != null){
            mOnScrollChangeLintener.onScrollChange(l, (int) (t * mSpeedUp), oldl, oldt);
        }
    }

    private int[] mMormalPadding = new int[4];

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMormalPadding[0] = getPaddingLeft();
        mMormalPadding[1] = getPaddingTop();
        mMormalPadding[2] = getPaddingRight();
        mMormalPadding[3] = getPaddingBottom();
    }

    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    // Log.v("mlguitar", "will up and animation");
                    animation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float preY = y;
                float nowY = ev.getY();
                /**
                 * size=4 表示 拖动的距离为屏幕的高度的1/4
                 */
                int deltaY = (int) (preY - nowY) / size;
                // 滚动
                // scrollBy(0, deltaY);

                y = nowY;
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isNeedMove()) {
                    if (normal.isEmpty()) {
                        // 保存正常的布局位置
                        normal.set(inner.getLeft(), inner.getTop(),
                                inner.getRight(), inner.getBottom());
                        return;
                    }
                    int yy = inner.getTop() - deltaY;

                    // 移动布局
                    Log.d(TAG, "commOnTouchEvent >>>>>>  yy - " + yy);
                    Log.d(TAG, "commOnTouchEvent >>>>>>  deltaY - " + deltaY);
                    if(deltaY < 0){
                        setPadding(mMormalPadding[0],mMormalPadding[1]-deltaY,mMormalPadding[2],mMormalPadding[3]);
                    }else{
                        setPadding(mMormalPadding[0],mMormalPadding[1],mMormalPadding[2]+deltaY,mMormalPadding[3]);
                    }
//                    inner.layout(inner.getLeft(), yy, inner.getRight(),
//                            inner.getBottom() - deltaY);
                }
                break;
            default:
                break;
        }
    }

    // 开启动画移动
    public void animation() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
                normal.top);
        ta.setDuration(200);
        inner.startAnimation(ta);
        // 设置回到正常的布局位置
        //inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        setPadding(mMormalPadding[0],mMormalPadding[1],mMormalPadding[2],mMormalPadding[3]);
        normal.setEmpty();
    }

    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    // 是否需要移动布局
    public boolean isNeedMove() {
        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        return scrollY == 0 || scrollY == offset;
    }

    public interface OnScrollChangeLintener{

        void onScrollChange(int l, int t, int oldl, int oldt);

    }
}
