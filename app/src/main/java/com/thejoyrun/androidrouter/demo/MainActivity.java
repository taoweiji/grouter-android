package com.thejoyrun.androidrouter.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.thejoyrun.router.Router;
import com.thejoyrun.router.RouterField;
import com.thejoyrun.router.Routers;

@Router(intExtra = {"uid", "age=>ago"}, longExtra = "time")
public class MainActivity extends AppCompatActivity {

    @RouterField
    private int uid;

    @RouterField
    private int age;

    @RouterField
    private long time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Routers.inject(this);
        setContentView(R.layout.activity_main);
    }
}
