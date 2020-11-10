package com.docstamp;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class Activity_Intro extends BaseActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }
}
