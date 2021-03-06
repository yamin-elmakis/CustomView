package com.example.customview;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import lib.yamin.easylog.EasyLog;
import lib.yamin.easylog.EasyLogFormatter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private VolumeView volumeView;
    private Button bPlus, bMinus;
    private ArcView arcView;
    private SonarView sonarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasyLog.showLogs(true);
        EasyLog.setFormatter(new EasyLogFormatter() {
            @Override
            public String format(String classname, String methodName, int lineNumber) {
                return String.format(Locale.getDefault(), "[%d] %s.%s() => ", lineNumber, classname, methodName);
            }
        });
        setContentView(R.layout.activity_main);
        getLayoutRes();
        initRes();
    }

    private void getLayoutRes() {
        volumeView = (VolumeView) findViewById(R.id.custom_view);
        bPlus = (Button) findViewById(R.id.b_plus);
        bMinus = (Button) findViewById(R.id.b_minus);
        arcView = (ArcView) findViewById(R.id.main_arc);
        sonarView = (SonarView) findViewById(R.id.main_sonar);
    }

    private void initRes() {
        bPlus.setOnClickListener(this);
        bMinus.setOnClickListener(this);
        arcView.setDestAngle(160, 1300);
        arcView.setHighColor(ContextCompat.getColor(this, R.color.yellow));
        arcView.setBackColorRes(R.color.red);
//        sonarView.startAnimation();
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
                EasyLog.d("arc");
                break;
        }
    }
}