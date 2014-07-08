package com.yamin.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TriangleView extends View{

	private Paint red;
	private Paint green;
	private Paint white;
	
	private Path bigT;
	private Path smallT;
	
	private final int MAX_HEIGHT = 100;
	private final int MAX_PERCENTAGE_VALUE = 1;
	private final int MIN_PERCENTAGE_VALUE = 0;
	private final int STROKE_WIDTH = 2;
	private int width;
	private float percentage = (float) 0;
	private double angle;

	public TriangleView(Context context) {
		super(context);
		initTrianglrView();
	}
	
	public TriangleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initTrianglrView();
	}

	public TriangleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initTrianglrView();
	}

	private void initTrianglrView() {
		setFocusable(true);

		red = new Paint(Paint.ANTI_ALIAS_FLAG);
		green = new Paint(Paint.ANTI_ALIAS_FLAG);
		white = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		setPaintParams(red, android.graphics.Color.RED);
		setPaintParams(green, android.graphics.Color.GREEN);
		setPaintParams(white, android.graphics.Color.WHITE);
		
		bigT = new Path();
		smallT = new Path();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		width = (int)getMeasuredWidth();
		angle = Math.atan2(MAX_HEIGHT, width);
		canvas.save();
		setPathParams(bigT, MAX_PERCENTAGE_VALUE);
		smallT.setFillType(Path.FillType.EVEN_ODD);
		setPathParams(smallT, percentage);
		
		canvas.drawPath(bigT, white);
		if (percentage > 0.8){
			canvas.drawPath(smallT, red);
		}else{
			canvas.drawPath(smallT, green);
		}
		
		canvas.restore();
	}
	
	private void setPaintParams(Paint paint, int color) {
		paint.setStrokeWidth(STROKE_WIDTH);
		paint.setColor(color);     
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setAntiAlias(true);
	}

	private void setPathParams(Path triangle, float percentage) {
		triangle.reset();
		triangle.moveTo(0,MAX_HEIGHT);						// go to start position
		triangle.lineTo(width * percentage, MAX_HEIGHT);  	// draw base line
		triangle.lineTo(width * percentage, MAX_HEIGHT - (float) (width *percentage* Math.tan(angle)));// draw height line
		triangle.lineTo(0,MAX_HEIGHT);						// draw the hypotenuse
		triangle.close();
	}
	
	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		if (percentage > MAX_PERCENTAGE_VALUE || percentage < MIN_PERCENTAGE_VALUE)
			return;
		this.percentage = (float)Math.round(percentage * 100) / 100;
	}
}
