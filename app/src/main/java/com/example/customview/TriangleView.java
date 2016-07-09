package com.example.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class TriangleView extends View {
    private final String TAG = "TriangleView";
    private final int DEFAULT_HEIGHT = 150;
    private final int MAX_PERCENTAGE_VALUE = 1;
    private final int MIN_PERCENTAGE_VALUE = 0;
    private final int STROKE_WIDTH = 2;
    private final float DELTA = (float) 0.05;
    private Paint red;
    private Paint green;
    private Paint white;
    private Path bigT;
    private Path smallT;
    private Drawable drawable;
    private int layoutWidth, layoutHeight;
    private float percentage = (float) 0;
    private double angle;

    public TriangleView(Context context) {
        this(context, null);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFocusable(true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TriangleView, 0, defStyle);
        drawable = a.getDrawable(R.styleable.TriangleView_android_src);
        if (drawable != null) {
            updateContentBounds();
            invalidate();
        }

        red = new Paint(Paint.ANTI_ALIAS_FLAG);
        green = new Paint(Paint.ANTI_ALIAS_FLAG);
        white = new Paint(Paint.ANTI_ALIAS_FLAG);

        setPaintParams(red, android.graphics.Color.RED);
        setPaintParams(green, android.graphics.Color.GREEN);
        setPaintParams(white, android.graphics.Color.WHITE);

        bigT = new Path();
        smallT = new Path();

        percentage = 0.5f;
        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.e(TAG, "onMeasure: widthSpec: "+widthMeasureSpec+" heightSpec: "+heightMeasureSpec);
//        Log.e(TAG, "onMeasure: MeasuredWidth: "+getMeasuredWidth());

        ViewGroup.LayoutParams viewParams = getLayoutParams();
        layoutWidth = viewParams.width;
        layoutHeight = viewParams.height;
        if (layoutWidth < 0)
            //Get the width measurement
            layoutWidth = View.resolveSize(getMeasuredWidth(), widthMeasureSpec);

        if (layoutHeight < 0)
            //Get the height measurement
            layoutHeight = View.resolveSize(DEFAULT_HEIGHT, heightMeasureSpec);

        Log.e(TAG, "onMeasure: layoutHeight: " + layoutHeight);
        Log.e(TAG, "onMeasure: layoutWidth: " + layoutWidth);

        //MUST call this to store the measurements
        setMeasuredDimension(layoutWidth, layoutHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged: oldw: " + oldw + " w: " + w + " oldh: " + oldh + " h: " + h);
        if (w != oldw || h != oldh) {
            updateContentBounds();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e(TAG, "onLayout: left: " + left + " top: " + top + " right: " + right + " bottom: " + bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, "onDraw");
//        width = (int)getMeasuredWidth();
        angle = Math.atan2(layoutHeight, layoutWidth);
        canvas.save();
        setPathParams(bigT, MAX_PERCENTAGE_VALUE);
        smallT.setFillType(Path.FillType.EVEN_ODD);
        setPathParams(smallT, percentage);

        canvas.drawPath(bigT, white);
        if (percentage > 0.8) {
            canvas.drawPath(smallT, red);
        } else {
            canvas.drawPath(smallT, green);
        }

        canvas.restore();

        if (drawable != null) {
            drawable.draw(canvas);
        }
    }

    private void updateContentBounds() {
        if (drawable == null) return;
        int left = (int) (layoutWidth * percentage - drawable.getIntrinsicWidth() / 2);
        int top = (int) (layoutHeight*(MAX_PERCENTAGE_VALUE - percentage) + (layoutHeight * percentage)/2 - drawable.getIntrinsicHeight() / 2);
        drawable.setBounds(left, top, left + drawable.getIntrinsicWidth(), top + drawable.getIntrinsicHeight());
    }

    private void setPaintParams(Paint paint, int color) {
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
    }

    private void setPathParams(Path triangle, float percentage) {
        triangle.reset();
        triangle.moveTo(0, layoutHeight);                        // go to start position
        triangle.lineTo(layoutWidth * percentage, layoutHeight);    // draw base line
        triangle.lineTo(layoutWidth * percentage, layoutHeight - (float) (layoutWidth * percentage * Math.tan(angle)));// draw height line
        triangle.lineTo(0, layoutHeight);                        // draw the hypotenuse
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
        setPercentage(getPercentage() + DELTA);
    }

    public void decrease() {
        setPercentage(getPercentage() - DELTA);
    }
}
