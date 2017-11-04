package com.example.customview;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Yamin on 29-Jan-17.
 */
public class ArcView extends View {

    private static final String TAG = "ArcView";
    private final Paint arcPaint, titlePaint, backPaint;
    private RectF arcRect;
    private PointF textPoint;
    private int layoutWidth, layoutHeight;
    private int arcAngle, dp5;
    private String value;
    private Rect textBounds;

    public ArcView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Set Angle to 0 initially
        arcAngle = 0;
        value = (arcAngle / 180 * 100) + "%";

        dp5 = (int) (5 * Resources.getSystem().getDisplayMetrics().density);

        backPaint = new Paint();
        backPaint.setAntiAlias(true);
        backPaint.setStyle(Paint.Style.STROKE);
        backPaint.setColor(Color.parseColor("#e1e5f0"));

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcRect = new RectF(0, 0, 1, 1);
        textPoint = new PointF(0, 0);

        textBounds = new Rect();
        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTextSize(21 * Resources.getSystem().getDisplayMetrics().scaledDensity);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 300;
        int desiredHeight = 150;

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
        this.setMeasuredDimension(layoutWidth, layoutHeight);

        float size = Math.min(layoutWidth, layoutHeight) / 10;
        arcPaint.setStrokeWidth(size);
        backPaint.setStrokeWidth(size);
        size = size / 2;
        arcRect.set(size, size, layoutWidth - size, 2*layoutHeight - size);
        textPoint.set(layoutWidth /2, 0.97f*layoutHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(arcRect, 180, 180, false, backPaint);
        canvas.drawArc(arcRect, 180, arcAngle, false, arcPaint);
        canvas.drawText(value, textPoint.x, textPoint.y, titlePaint);
    }

    private void setArcAngle(int arcAngle) {
        if (arcAngle > 0 && arcAngle < 180 && this.arcAngle != arcAngle) {
            this.arcAngle = arcAngle;
            value = String.valueOf((arcAngle * 100 / 180)) + "%";

            titlePaint.getTextBounds(value, 0, value.length(), textBounds);
//            textPoint.x = layoutWidth /2 - textBounds.width() / 2;
            invalidate();
        }
    }

    public void setDestAngle(int destAngle) {
        if (destAngle < 72) {
            titlePaint.setColor(Color.parseColor("#E53935"));
            arcPaint.setColor(Color.parseColor("#E53935"));
        } else if (destAngle < 125) {
            titlePaint.setColor(Color.parseColor("#FBC02D"));
            arcPaint.setColor(Color.parseColor("#FBC02D"));
        } else {
            titlePaint.setColor(Color.parseColor("#00E676"));
            arcPaint.setColor(Color.parseColor("#00E676"));

        }
        ValueAnimator anim = ValueAnimator.ofObject(new IntEvaluator(), 0, destAngle);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setArcAngle((Integer) animation.getAnimatedValue());
            }
        });
        anim.setDuration(1000);
        anim.start();
    }
}