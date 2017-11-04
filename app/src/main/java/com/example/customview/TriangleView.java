package com.example.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Yamin on 25-Sep-17.
 */
public class TriangleView extends View {
    private final String TAG = "TriangleView";
    private Paint paint;
    private Path path;
    private int layoutWidth, layoutHeight;

    public TriangleView(Context context) {
        this(context, null);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFocusable(true);

        int color = Color.WHITE;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TriangleView);
            color = ta.getColor(R.styleable.TriangleView_triangle_color, Color.WHITE);
            ta.recycle();
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setPaintParams(paint, color);
        path = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 100;
        int desiredHeight = 100;

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
        setPathParams(path);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    private void setPaintParams(Paint paint, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    private void setPathParams(Path triangle) {
        triangle.reset();
        triangle.moveTo(getPaddingLeft(), getPaddingTop());
        triangle.lineTo(layoutWidth - getPaddingRight(), getPaddingTop());
        triangle.lineTo(layoutWidth / 2, layoutHeight - getPaddingBottom());
        triangle.lineTo(getPaddingLeft(), getPaddingTop());
        triangle.close();
    }

    public void setColorRes(@ColorRes int color) {
        setColor(ContextCompat.getColor(getContext(), color));
    }

    public void setColor(int color) {
        paint.setColor(color);
        invalidate();
    }
}