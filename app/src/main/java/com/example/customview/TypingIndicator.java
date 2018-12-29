package com.example.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import lib.yamin.easylog.EasyLog;

/**
 * Created by Yamin on 19-Apr-18.
 */
public class TypingIndicator extends View implements Animatable {

    float circleSpacing = 3, radius, startX;
    private int layoutWidth, layoutHeight;
    public static final int DURATION = 1000;
    public static final float JUMP_FACTOR = 2.7f;
    private int dotsCount;
    private float[] dotY;
    private float[] delta;
    private ValueAnimator animator;
    private ValueAnimator.AnimatorUpdateListener animatorListener;

    private Paint mPaint = new Paint();

    public TypingIndicator(Context context) {
        super(context);
        init(context, null, 0);
    }

    public TypingIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TypingIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        EasyLog.e();
        int color = Color.WHITE;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TypingIndicator);
            color = ta.getColor(R.styleable.TypingIndicator_indicator_color, Color.WHITE);
            ta.recycle();
        }

        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        dotsCount = 3;

        animator = ValueAnimator.ofInt(0, 220);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(DURATION);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
//        animator.setStartDelay(DELAY);

        dotY = new float[dotsCount];
        delta = new float[dotsCount];
        animatorListener = new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();

                delta[0] = toRadians(value);
                delta[1] = toRadians(value - 20);
                delta[2] = toRadians(value - 40);
                postInvalidate();
            }

            private float toRadians(int angle) {
                if (angle > 180 || angle < 0) return 0;
                return (float) Math.toRadians(angle);
            }
        };
        start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float dp5 = 5 * Resources.getSystem().getDisplayMetrics().density;
        int desiredHeight = (int) (dotsCount * dp5);
        int desiredWidth = (int) (dotsCount * dp5 + dotsCount * circleSpacing);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            layoutWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            layoutWidth = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            layoutWidth = desiredWidth;
        }
        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            layoutHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            layoutHeight = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            layoutHeight = desiredHeight;
        }

        setMeasuredDimension(layoutWidth, layoutHeight);
        radius = (float) ((Math.min(layoutWidth, layoutHeight) - circleSpacing * 2) / (2 * Math.ceil(JUMP_FACTOR)));
        startX = layoutWidth / 2 - (radius * 2 + circleSpacing);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < dotsCount; i++) {
            dotY[i] = (float) (layoutHeight - radius - ((Math.sin(delta[i])) * JUMP_FACTOR * radius));
            canvas.save();
            float translateX = startX + (radius * 2) * i + circleSpacing * i;
            canvas.translate(translateX, dotY[i]);
            canvas.drawCircle(0, 0, radius, mPaint);
            canvas.restore();
        }
    }

    private boolean isStarted() {
        if (animator == null) return false;

        return animator.isStarted();
    }

    @Override
    public void start() {
        if (isStarted()) {
            return;
        }
        if (animator == null) {
            return;
        }
        animator.addUpdateListener(animatorListener);
        animator.start();
        invalidate();
    }

    @Override
    public void stop() {
        if (!isStarted()) return;

        animator.removeAllUpdateListeners();
        animator.end();
        animator = null;
    }

    @Override
    public boolean isRunning() {
        if (animator == null) return false;

        return animator.isRunning();
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        EasyLog.e();
        super.onDetachedFromWindow();
    }
}
