package com.example.customview.views.basic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.example.customview.R;

import lib.yamin.easylog.EasyLog;

/**
 * Created by Yamin on 25-May-18.
 */
public class StarView extends View {
    private final int NUMBER_OF_POINTS = 5;
    private Paint startPaint, endPaint;
    private Path startPath, endPath;
    private Point center;
    private int outerRadius, innerRadius;
    private float delta;

    public StarView(Context context) {
        this(context, null);
    }

    public StarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFocusable(true);

        int startColor = Color.WHITE;
        int endColor = Color.WHITE;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StarView);
            startColor= ta.getColor(R.styleable.StarView_star_start_color, Color.WHITE);
            endColor= ta.getColor(R.styleable.StarView_star_end_color, Color.WHITE);
            ta.recycle();
        }

        startPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        startPaint.setColor(startColor);
        startPaint.setStyle(Paint.Style.FILL);
        startPaint.setAntiAlias(true);

        endPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        endPaint.setColor(endColor);
        endPaint.setStyle(Paint.Style.FILL);
        endPaint.setAntiAlias(true);

        startPath = new Path();
        endPath = new Path();
        center = new Point(0, 0);
        delta = (float) (2f * Math.PI / (2 * NUMBER_OF_POINTS));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int layoutWidth, layoutHeight;

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
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            center.x = w / 2;
            center.y = h / 2;
            outerRadius = Math.min(w, h) / 2;
            innerRadius = (int) (outerRadius / 2);
            setPathParams(startPath, true);
            setPathParams(endPath, false);
        }
    }

    private void setPathParams(Path path, boolean isStart) {
        path.reset();
        path.moveTo((float) (center.x), (float) (center.y - outerRadius));
        boolean isOuter = true;
        float section;
        double destAngle;
        if (isStart) {
            section = (float) Math.PI;
            destAngle = 2 * Math.PI;
        } else {
            destAngle = Math.PI + delta / 2;
            section = delta;
        }
        double angle;
        while (section <= destAngle) {
            if (isOuter) {
                // innerPoint
                angle = Math.PI / 2 - section;
                path.lineTo((float) (center.x + innerRadius * Math.cos(angle)),
                        (float) (center.y - innerRadius * Math.sin(angle)));
                section += delta;
            } else {
                // outer point
                angle = Math.PI / 2 - section;
                path.lineTo((float) (center.x + outerRadius * Math.cos(angle)),
                        (float) (center.y - outerRadius * Math.sin(angle)));
                section += delta;
            }
            isOuter = !isOuter;
        }
        path.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(startPath, startPaint);
        canvas.drawPath(endPath, endPaint);
    }
}
