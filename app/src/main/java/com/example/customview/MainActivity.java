package com.example.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TriangleView triangle;
    private Button bPlus, bMinus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLayoutRes();
        initRes();
    }

    /**
     * get the objects from the layout
     */
    private void getLayoutRes() {
        triangle = (TriangleView) findViewById(R.id.custom_view);
        bPlus = (Button) findViewById(R.id.b_plus);
        bMinus = (Button) findViewById(R.id.b_minus);
    }

    /**
     * initiate the objects
     */
    private void initRes() {
        bPlus.setOnClickListener(this);
        bMinus.setOnClickListener(this);
    }

    @Override
    public void onClick(View btn) {
        switch (btn.getId()) {
            case R.id.b_plus:
                triangle.increase();
                break;
            case R.id.b_minus:
                triangle.decrease();
                break;
        }
    }
}