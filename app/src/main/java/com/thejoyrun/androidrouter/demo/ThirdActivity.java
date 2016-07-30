package com.thejoyrun.androidrouter.demo;

import android.os.Bundle;

import com.thejoyrun.router.RouterActivity;

@RouterActivity("third")
public class ThirdActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
    }
}
