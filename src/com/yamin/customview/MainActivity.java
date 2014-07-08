package com.yamin.customview;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private TriangleView triangle;
	private Button bPlus, bMnius;
	private static final float DELTA = (float) 0.05;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getLayoutRes();
		initRes();
	}

	/** get the objects from the layout */
	private void getLayoutRes() {
		triangle = (TriangleView) findViewById(R.id.CustomView);
		bPlus = (Button) findViewById(R.id.b_plus);
		bMnius = (Button) findViewById(R.id.b_minus);
	}
	
	/** initiate the objects  */
	private void initRes(){
		triangle.setPercentage((float) 0.5);
		triangle.getLayoutParams().height = 150;
		bPlus.setOnClickListener(new buttonClicked());
		bMnius.setOnClickListener(new buttonClicked());
	}
	
	class buttonClicked implements View.OnClickListener{

		@Override
		public void onClick(View btn) {
			switch (btn.getId()) {
			case R.id.b_plus:
				triangle.setPercentage(triangle.getPercentage() + DELTA);
				break;
			case R.id.b_minus:
				triangle.setPercentage(triangle.getPercentage() - DELTA);
				break;
			}
			triangle.invalidate();
		}
	}
}
