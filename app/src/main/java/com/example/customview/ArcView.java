package com.example.customview;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import static androidx.core.content.ContextCompat.getColor;

/**
 * Created by Yamin on 29-Jan-17.
 */
public class ArcView extends View {

    private static final String TAG = "ArcView";
    private final Paint arcPaint, titlePaint, backPaint;
    private RectF arcRect;
    private PointF textPoint;
    private int layoutWidth, layoutHeight;
    private int arcAngle, dp5, textSize, arcWidth;
    private String value;
    private Rect textBounds;
    private int textColor, lowColor, midColor, highColor, backColor;

    public ArcView(Context context) {
        this(context, null);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Set Angle to 0 initially
        arcAngle = 0;
        value = (arcAngle / 180 * 100) + "%";

        dp5 = (int) (5 * Resources.getSystem().getDisplayMetrics().density);

        textColor = 0;
        lowColor = Color.RED;
        midColor = Color.YELLOW;
        highColor = Color.GREEN;
        backColor = Color.GRAY;
        textSize = -1;
        arcWidth = -1;

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ArcView);
            textColor = ta.getColor(R.styleable.ArcView_android_textColor, textColor);
            lowColor = ta.getColor(R.styleable.ArcView_arc_low_color, lowColor);
            midColor = ta.getColor(R.styleable.ArcView_arc_mid_color, midColor);
            highColor = ta.getColor(R.styleable.ArcView_arc_high_color, highColor);
            backColor = ta.getColor(R.styleable.ArcView_arc_back_color, backColor);
            textSize = ta.getDimensionPixelSize(R.styleable.ArcView_android_textSize, textSize);
            arcWidth = ta.getDimensionPixelSize(R.styleable.ArcView_arc_width, arcWidth);
            ta.recycle();
        }

        backPaint = new Paint();
        backPaint.setAntiAlias(true);
        backPaint.setStyle(Paint.Style.STROKE);
        setBackColor(backColor);

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcRect = new RectF(0, 0, 1, 1);
        textPoint = new PointF(0, 0);

        textBounds = new Rect();
        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        if (textColor != 0)
            setTextColor(textColor);
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

        float size = arcWidth > 0 ? arcWidth : Math.min(layoutWidth, layoutHeight) / 10;
        float textFinalSize = textSize > 0 ? textSize : size * Resources.getSystem().getDisplayMetrics().scaledDensity;
        arcPaint.setStrokeWidth(size);
        backPaint.setStrokeWidth(size);
        titlePaint.setTextSize(textFinalSize);
        size = size / 2;
        arcRect.set(size + getPaddingLeft(),
                size + getPaddingTop(),
                layoutWidth - size - getPaddingRight(),
                2 * layoutHeight - size - 2 * getPaddingBottom() - getPaddingTop());
        textPoint.set(layoutWidth / 2, 0.97f * layoutHeight);
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
        setDestAngle(destAngle, 1000);
    }

    public void setDestAngle(int destAngle, long durationInMillis) {
        if (destAngle < 72) {
            arcPaint.setColor(lowColor);
            setTextColor(textColor != 0 ? textColor : lowColor);
        } else if (destAngle < 125) {
            arcPaint.setColor(midColor);
            setTextColor(textColor != 0 ? textColor : midColor);
        } else {
            arcPaint.setColor(highColor);
            setTextColor(textColor != 0 ? textColor : highColor);
        }
        ValueAnimator anim = ValueAnimator.ofObject(new IntEvaluator(), 0, destAngle);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setArcAngle((Integer) animation.getAnimatedValue());
            }
        });
        anim.setDuration(durationInMillis);
        anim.start();
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getArcWidth() {
        return arcWidth;
    }

    public void setArcWidth(int arcWidth) {
        this.arcWidth = arcWidth;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColorRes(@ColorRes int textColor) {
        setTextColor(ContextCompat.getColor(getContext(), textColor));
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        titlePaint.setColor(textColor);
    }

    public int getLowColor() {
        return lowColor;
    }

    public void setLowColorRes(@ColorRes int lowColor) {
        setLowColor(ContextCompat.getColor(getContext(), lowColor));
    }

    public void setLowColor(int lowColor) {
        this.lowColor = lowColor;
    }

    public int getMidColor() {
        return midColor;
    }

    public void setMidColorRes(@ColorRes int midColor) {
        setMidColor(ContextCompat.getColor(getContext(), midColor));
    }

    public void setMidColor(int midColor) {
        this.midColor = midColor;
    }

    public int getHighColor() {
        return highColor;
    }

    public void setHighColor(int highColor) {
        this.highColor = highColor;
    }

    public void setHighColorRes(@ColorRes int highColor) {
        this.highColor = getColor(getContext(), highColor);
    }

    public int getBackColor() {
        return backColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
        backPaint.setColor(backColor);
    }

    public void setBackColorRes(int backColor) {
        setBackColor(ContextCompat.getColor(getContext(), backColor));
    }
}