package com.twopai.waterdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WaterActivity extends AppCompatActivity {

    private WaterView waterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        waterView = (WaterView) findViewById(R.id.waterView);
        waterView.startAnim(waterView);
    }
}
