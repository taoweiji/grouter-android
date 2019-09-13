//package com.grouter.demo;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//
//import com.grouter.RouterField;
//
///**
// * Created by Wiki on 19/7/30.
// */
//public class BaseActivity extends AppCompatActivity {
//    @RouterField("formActivity")
//    protected String formActivity;
//
//
//    @Override
//    public void startForResult(Intent intent, int requestCode, Bundle options) {
//        intent.putExtra("formActivity", getClass().getName());
//        super.startForResult(intent, requestCode, options);
//    }
//}
