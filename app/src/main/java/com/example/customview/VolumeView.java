package com.example.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class VolumeView extends View {
    private final static int MAX_PERCENTAGE_VALUE = 1;
    private final static int MIN_PERCENTAGE_VALUE = 0;

    private Paint highPaint, normalPaint, backPaint;
    private Path bigT, smallT;
    private Drawable drawable;
    private int layoutWidth, layoutHeight;
    private float percentage, highPercentage, delta;
    private double angle;

    public VolumeView(Context context) {
        this(context, null);
    }

    public VolumeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VolumeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFocusable(true);

        highPercentage = 0.8f;
        percentage = 0.5f;
        delta = 0.05f;
        int backColor = android.graphics.Color.WHITE;
        int normalColor = android.graphics.Color.GREEN;
        int highColor = android.graphics.Color.RED;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VolumeView, 0, defStyle);
            backColor = ta.getColor(R.styleable.VolumeView_volume_back_color, backColor);
            normalColor = ta.getColor(R.styleable.VolumeView_volume_normal_color, normalColor);
            highColor = ta.getColor(R.styleable.VolumeView_volume_high_color, highColor);
            highPercentage = ta.getFloat(R.styleable.VolumeView_volume_high_percentage, highPercentage);
            percentage = ta.getFloat(R.styleable.VolumeView_volume_base_percentage, percentage);
            delta = ta.getFloat(R.styleable.VolumeView_volume_delta, delta);
            drawable = ta.getDrawable(R.styleable.VolumeView_android_src);
            if (drawable != null) {
                updateContentBounds();
                invalidate();
            }
            ta.recycle();
        }

        highPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        normalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        setPaintParams(highPaint, highColor);
        setPaintParams(normalPaint, normalColor);
        setPaintParams(backPaint, backColor);

        bigT = new Path();

        smallT = new Path();
        smallT.setFillType(Path.FillType.EVEN_ODD);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 500;
        int desiredHeight = 200;

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
        angle = Math.atan2(layoutHeight, layoutWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            updateContentBounds();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setPathParams(bigT, MAX_PERCENTAGE_VALUE);
        setPathParams(smallT, percentage);

        canvas.drawPath(bigT, backPaint);

        if (percentage > highPercentage) {
            canvas.drawPath(smallT, highPaint);
        } else {
            canvas.drawPath(smallT, normalPaint);
        }

        if (drawable != null) {
            drawable.draw(canvas);
        }
    }

    private void updateContentBounds() {
        if (drawable == null) return;

        int left = (int) (layoutWidth * percentage - drawable.getIntrinsicWidth() / 2);
        int top = (int) (layoutHeight * (MAX_PERCENTAGE_VALUE - percentage) + (layoutHeight * percentage) / 2 - drawable.getIntrinsicHeight() / 2);
        drawable.setBounds(left, top, left + drawable.getIntrinsicWidth(), top + drawable.getIntrinsicHeight());
    }

    private void setPaintParams(Paint paint, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
    }

    private void setPathParams(Path triangle, float percentage) {
        triangle.reset();
        triangle.moveTo(0, layoutHeight);                           // go to start position
        triangle.lineTo(layoutWidth * percentage, layoutHeight);    // draw base line
        triangle.lineTo(layoutWidth * percentage, layoutHeight -
                (float) (layoutWidth * percentage * Math.tan(angle)));// draw height line
        triangle.lineTo(0, layoutHeight);                            // draw the hypotenuse
        triangle.close();
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        if (percentage > MAX_PERCENTAGE_VALUE || percentage < MIN_PERCENTAGE_VALUE)
            return;
        this.percentage = (float) Math.round(percentage * 100) / 100;
        updateContentBounds();
        invalidate();
    }

    public void increase() {
        setPercentage(getPercentage() + delta);
    }

    public void decrease() {
        setPercentage(getPercentage() - delta);
    }
}
