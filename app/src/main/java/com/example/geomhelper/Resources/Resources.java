package com.example.geomhelper.Resources;

import android.graphics.Color;

import com.example.geomhelper.R;

public class Resources {

    public int[] colors = new int[6];
    public int[] background = new int[4];

    public Resources() {

        colors[5]= Color.parseColor("#0864F6");
        colors[4]= Color.parseColor("#1283F4");
        colors[3]= Color.parseColor("#2196F3");
        colors[2]= Color.parseColor("#03A9F4");
        colors[1]= Color.parseColor("#32BAF3");
        colors[0]= Color.parseColor("#43CBF7");

        background[3] = R.drawable.background_test_4;
        background[2] = R.drawable.background_test_3;
        background[1] = R.drawable.background_test_2;
        background[0] = R.drawable.background_test_1;

    }
}
