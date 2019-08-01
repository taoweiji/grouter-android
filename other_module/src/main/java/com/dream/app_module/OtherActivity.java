package com.dream.app_module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.thejoyrun.router.RouterActivity;

@RouterActivity("other")
public class OtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_module);
    }
}
