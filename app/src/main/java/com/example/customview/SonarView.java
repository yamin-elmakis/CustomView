package com.example.customview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

import lib.yamin.easylog.EasyLog;

/**
 * Created by Yamin on 2/5/2018.
 */
public class SonarView extends View {

    private final int POINT_ARRAY_SIZE = 60;

    private int center, radius, dotRadius, alphaStep, alphaDirection;
    private Random random;
    float angle = 0;
    private Point dotPoint;
    private Paint dotPaint, linePaint;

    android.os.Handler mHandler = new android.os.Handler();
    Runnable mTick = new Runnable() {
        @Override
        public void run() {
            invalidate();
            mHandler.postDelayed(this, 17);
        }
    };

    public SonarView(Context context) {
        this(context, null);
    }

    public SonarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SonarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        dotPaint = new Paint();
        dotPaint.setColor(Color.WHITE);
        dotPaint.setAntiAlias(true);
        dotPaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(Resources.getSystem().getDisplayMetrics().density);

        alphaStep = 255 / POINT_ARRAY_SIZE;
        random = new Random(System.currentTimeMillis());
        dotPoint = new Point();
        alphaDirection = 1;

        startAnimation();
    }

    public void startAnimation() {
        mHandler.removeCallbacks(mTick);
        mHandler.post(mTick);
    }

    public void stopAnimation() {
        mHandler.removeCallbacks(mTick);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int layoutWidth, layoutHeight;

        int desiredWidth = (int) (100*Resources.getSystem().getDisplayMetrics().density);
        int desiredHeight = (int) (100*Resources.getSystem().getDisplayMetrics().density);

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
        center = Math.min(layoutWidth, layoutHeight) / 2;
        radius = (int) (Math.min(layoutWidth, layoutHeight) / 2 - Resources.getSystem().getDisplayMetrics().density);
        dotRadius = radius / 9;
        EasyLog.e("center: "+center);
        EasyLog.e("radius: "+radius);
        updateDot();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw the circles
        linePaint.setAlpha(255);
        canvas.drawCircle(center, center, radius, linePaint);
        canvas.drawCircle(center, center, radius * 3 / 4, linePaint);
        canvas.drawCircle(center, center, radius >> 1, linePaint);
        canvas.drawCircle(center, center, radius >> 2, linePaint);

        // update the rotation
        angle += 1;
        if (angle >= 360) angle = 0;

        // draw the sonar
        for (int x = 0; x < POINT_ARRAY_SIZE; x++) {
            linePaint.setAlpha(alphaStep * (x+1));
            canvas.save();
            canvas.rotate(angle + x, center, center);
            canvas.drawLine(center, center, center + radius, center, linePaint);
            canvas.restore();
        }

        // draw the dot
        int dotAlpha = dotPaint.getAlpha();
        if (alphaDirection > 0 && dotAlpha >= 250)
            alphaDirection = -3;
        else if (alphaDirection < 0 && dotAlpha <= 4) {
            updateDot();
            alphaDirection = 3;
        }
        dotAlpha += alphaDirection;
        dotPaint.setAlpha(dotAlpha);
        canvas.drawCircle(dotPoint.x, dotPoint.y, dotRadius, dotPaint);
    }

    private void updateDot() {
        dotPoint.set(center + dotRadius+ (int) ((radius >> 1) * Math.sin(random.nextInt(7))),
                center + dotRadius+(int) ((radius >> 1) * Math.sin(random.nextInt(7))));
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }
}
