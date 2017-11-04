package com.example.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private VolumeView volumeView;
    private Button bPlus, bMinus;
    private ArcView arcView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLayoutRes();
        initRes();
    }

    private void getLayoutRes() {
        volumeView = (VolumeView) findViewById(R.id.custom_view);
        bPlus = (Button) findViewById(R.id.b_plus);
        bMinus = (Button) findViewById(R.id.b_minus);
        arcView = (ArcView) findViewById(R.id.main_arc);
    }

    private void initRes() {
        bPlus.setOnClickListener(this);
        bMinus.setOnClickListener(this);
        arcView.setDestAngle(160);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_plus:
                volumeView.increase();
                break;

            case R.id.b_minus:
                volumeView.decrease();
                break;

            case R.id.main_arc:
                Log.d(TAG, "onClick: arc");
                break;
        }
    }
}