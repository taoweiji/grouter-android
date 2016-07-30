package com.thejoyrun.androidrouter.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.thejoyrun.router.RouterField;
import com.thejoyrun.router.Routers;

public class SecondActivity extends AppCompatActivity {

    @RouterField("uid")
    private int uid;

    @RouterField("age")
    private int age;

    @RouterField("time")
    private long time;

    @RouterField("name")
    private String name;
    @RouterField("man")
    private Boolean man = true;

    @RouterField("manger")
    private Boolean manger;

    @RouterField("user")
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Routers.inject(this);
        Log.e("uid", String.valueOf(uid));
        Log.e("age", String.valueOf(age));
        Log.e("time", String.valueOf(time));
        Log.e("name", String.valueOf(name));
        Log.e("man", String.valueOf(man));
        Log.e("manger", String.valueOf(manger));
//        Log.e("user", user.toString());
    }
}
