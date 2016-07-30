package com.thejoyrun.androidrouter.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.thejoyrun.router.Router;
import com.thejoyrun.router.RouterField;

@Router(intExtra = {"uid", "age=>ago"}, longExtra = "time")
public class MainActivity extends AppCompatActivity {

    @RouterField("uid")
    private int uid;

    @RouterField("uid")
    private int age;

    @RouterField("uid")
    private long time;

    @RouterField("name")
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("uid", "233");
                intent.putExtra("age", "24");
                intent.putExtra("time", System.currentTimeMillis());
                intent.putExtra("name", "Wiki");
                intent.putExtra("man", "true");
                intent.putExtra("manger", "true");
                intent.putExtra("user", new User("Wiki2"));
                startActivity(intent);
            }
        });
//        Routers.inject(this);

    }
}
