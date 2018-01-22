/*
 *  Copyright 2016 Eric Liu
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package merlin.yx.com.common.widget.ripple;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * A layout that implemented ripple effect.
 * <p>
 * Created by Eric on 2016/11/2.
 */
public class RippleView extends View {

    private static final int DEFAULT_DURATION = 250;

    private Paint paint;
    private ObjectAnimator animator;
    private int duration = DEFAULT_DURATION;
    private int centerX = -1;
    private int centerY = -1;
    private int radius;
    private boolean isAnimationEnd = true;
    private boolean isOpened = false;

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
    }

    public boolean isAnimationEnd() {
        return isAnimationEnd;
    }

    @Override
    public void setBackgroundColor(int color) {
        paint.setColor(color);
    }

    /**
     * Return the max radius of the ripple.
     *
     * @return
     */
    private double computeRadius() {
        double radius = Math.sqrt(getHeight() * getHeight() + getWidth() * getWidth()) / 2;
        return radius;
    }

    private Point point;

    public void start(Point point) {
        this.point = point;
        start(point.x, point.y);
    }

    private void start(int startX, int startY) {
        isAnimationEnd = false;
        isOpened = false;
        centerX = startX;
        centerY = startY;
        double radius = computeRadius();

        PropertyValuesHolder radiusPvh = PropertyValuesHolder.ofInt("radius", 0, (int) radius);
        PropertyValuesHolder centerXPvh = PropertyValuesHolder.ofInt("centerX", startX, getWidth() / 2);
        PropertyValuesHolder centerYPvh = PropertyValuesHolder.ofInt("centerY", startY, getHeight() / 2);
        PropertyValuesHolder alphaPvh = PropertyValuesHolder.ofFloat("alpha",0,1);
        animator = ObjectAnimator.ofPropertyValuesHolder(this, radiusPvh, centerXPvh, centerYPvh,alphaPvh);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onOpened();
            }
        });
        animator.start();
    }

    public boolean canBack() {
        return point != null;
    }


    public void back() {
        if (canBack()) {
            back(point.x, point.y);
        }
    }

    public void setPoint(int[] location) {
        this.point.x = location[0];
        this.point.y = location[1];
    }

    public void back(Point point) {
        back(point.x, point.y);
    }

    private void back(int startX, int startY) {
        isAnimationEnd = false;
        isOpened = false;
        centerX = startX;
        centerY = startY;
        double radius = computeRadius();

        PropertyValuesHolder radiusPvh = PropertyValuesHolder.ofInt("radius", (int) radius, 0);
        PropertyValuesHolder centerXPvh = PropertyValuesHolder.ofInt("centerX", getWidth() / 2, startX);
        PropertyValuesHolder centerYPvh = PropertyValuesHolder.ofInt("centerY", getHeight() / 2, startY);
        animator = ObjectAnimator.ofPropertyValuesHolder(this, radiusPvh, centerXPvh, centerYPvh);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onClosed();
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isOpened) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        } else {
            canvas.drawCircle(centerX, centerY, radius, paint);
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
        invalidate();
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
        invalidate();
    }

    private void onOpened() {
        isOpened = true;
        isAnimationEnd = true;
        invalidate();

        if (mOnStateChangedListener != null) {
            mOnStateChangedListener.onOpened();
        }
    }

    private void onClosed() {
        isOpened = false;
        isAnimationEnd = true;
        setVisibility(View.GONE);

        if (mOnStateChangedListener != null) {
            mOnStateChangedListener.onClosed();
        }
    }

    public OnStateChangedListener mOnStateChangedListener;

    public void setmOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.mOnStateChangedListener = onStateChangedListener;
    }

    public interface OnStateChangedListener {

        void onOpened();

        void onClosed();

    }

}
