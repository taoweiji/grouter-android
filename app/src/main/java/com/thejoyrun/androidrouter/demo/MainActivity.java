package com.thejoyrun.androidrouter.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.thejoyrun.router.Routers;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("uid", "233");
                intent.putExtra("age", "24");
                intent.putExtra("time", System.currentTimeMillis());
                intent.putExtra("name", "Wiki");
                intent.putExtra("man", "true");
                intent.putExtra("manger", "true");
                startActivity(intent);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.setData(Uri.parse("joyrun://second?uid=233&age=24"));
                startActivity(intent);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.setData(Uri.parse("joyrun://second?uid=233&age=24"));
                intent.putExtra("time", System.currentTimeMillis());
                intent.putExtra("name", "Wiki");
                intent.putExtra("man", "true");
                intent.putExtra("manger", "true");
                startActivity(intent);
            }
        });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Routers.startActivity(MainActivity.this, "joyrun://second?uid=233&age=24");
            }
        });
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("joyrun://second?uid=233&age=24")));
            }
        });

    }
}
