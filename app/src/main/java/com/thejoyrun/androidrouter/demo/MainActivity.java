package com.thejoyrun.androidrouter.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.thejoyrun.router.Routers;
import com.thejoyrun.router.RoutersHelper;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1) {
            Intent intent = new Intent(this, SecondActivity.class);
            intent.putExtra("uid", "233");
            intent.putExtra("age", "24");
            intent.putExtra("time", System.currentTimeMillis());
            intent.putExtra("name", "Wiki");
            intent.putExtra("man", "true");
            intent.putExtra("manger", "true");
            startActivity(intent);
        } else if (v.getId() == R.id.button2) {
            Intent intent = new Intent(this, SecondActivity.class);
            intent.setData(Uri.parse("joyrun://second?uid=233&age=24"));
            startActivity(intent);
        } else if (v.getId() == R.id.button3) {
            Intent intent = new Intent(this, SecondActivity.class);
            intent.setData(Uri.parse("joyrun://second?uid=233&age=24"));
            intent.putExtra("time", System.currentTimeMillis());
            intent.putExtra("name", "Wiki");
            intent.putExtra("man", "true");
            intent.putExtra("manger", "true");
            startActivity(intent);
        } else if (v.getId() == R.id.button4) {
            Routers.startActivity(this, "joyrun://second/third?uid=233&age=24");
        } else if (v.getId() == R.id.button5) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("joyrun://second?uid=233&age=24")));
        } else if (v.getId() == R.id.button6) {
//                Routers.startActivity(this, "joyrun://www.thejoyrun.com/second?uid=233&age=24");
//                Routers.startActivity(this, "other2://www.thejoyrun.com/second?uid=233&age=24");
            Routers.startActivity(this, "joyrun://www.thejoyrun.com/second?uid=233&age=24");
        } else if (v.getId() == R.id.button7) {
            RoutersHelper.getSecondActivityHelper().withUid(24).withName("Wiki").start(this);
        }
    }
}
