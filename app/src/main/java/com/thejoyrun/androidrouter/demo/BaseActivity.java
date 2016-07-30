package com.thejoyrun.androidrouter.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.thejoyrun.router.RouterField;

/**
 * Created by Wiki on 16/7/30.
 */
public class BaseActivity extends AppCompatActivity {
    @RouterField("formActivity")
    protected String formActivity;


    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        intent.putExtra("formActivity", getClass().getName());
        super.startActivityForResult(intent, requestCode, options);
    }
}
