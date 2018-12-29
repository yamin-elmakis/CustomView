package com.example.customview.views.basic;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.customview.R;

/**
 * Created by Yamin on 20-Oct-17.
 */

public class CircleView extends View {

    private final Paint topPaint, bottomPaint;
    private RectF rect;
    private int dp5;
    private int strokeWidth;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        dp5 = (int) (5 * Resources.getSystem().getDisplayMetrics().density);

        int topColor = Color.YELLOW;
        int bottomColor = Color.BLACK;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
            topColor = ta.getColor(R.styleable.CircleView_circle_top_color, topColor);
            bottomColor = ta.getColor(R.styleable.CircleView_circle_bottom_color, bottomColor);
            strokeWidth = ta.getDimensionPixelSize(R.styleable.CircleView_circle_stroke_width, 0);
            ta.recycle();
        }

        bottomPaint = new Paint();
        bottomPaint.setAntiAlias(true);
        bottomPaint.setStyle(Paint.Style.STROKE);
        bottomPaint.setColor(bottomColor);

        topPaint = new Paint();
        topPaint.setAntiAlias(true);
        topPaint.setStyle(Paint.Style.STROKE);
        topPaint.setColor(topColor);

        rect = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 5 * dp5;
        int desiredHeight = 5 * dp5;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }
        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);

        if (strokeWidth <= 0)
            strokeWidth = Math.min(width, height) / 5;

        topPaint.setStrokeWidth(strokeWidth);
        bottomPaint.setStrokeWidth(strokeWidth);

        int strokeSpace = strokeWidth / 2;

        rect.set(strokeSpace + getPaddingLeft(),
                strokeSpace + getPaddingTop(),
                width - strokeSpace - getPaddingRight(),
                height - strokeSpace - getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(rect, 179, 182, false, topPaint);
        canvas.drawArc(rect, 0, 181, false, bottomPaint);
    }
}
