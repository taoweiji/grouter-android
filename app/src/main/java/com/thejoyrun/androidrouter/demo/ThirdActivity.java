package com.thejoyrun.androidrouter.demo;

import android.os.Bundle;
import android.widget.TextView;

import com.thejoyrun.router.Router;
import com.thejoyrun.router.RouterActivity;
import com.thejoyrun.router.RouterField;

@RouterActivity("third")
public class ThirdActivity extends BaseActivity {
    @RouterField("uid")
    private int uid;

    @RouterField("age")
    private int age;

//    @RouterField("time")
//    private long time;

    @RouterField("name")
    private String name;
    @RouterField("man")
    private Boolean man = true;

    @RouterField("manger")
    private Boolean manger;

    //    @RouterField("user")
//    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        Router.inject(this);
        StringBuilder builder = new StringBuilder();
        builder.append("uid:" + String.valueOf(uid)).append('\n');
        builder.append("age:" + String.valueOf(age)).append('\n');
        builder.append("name:" + String.valueOf(name)).append('\n');
        builder.append("man:" + String.valueOf(man)).append('\n');
        builder.append("manger:" + String.valueOf(manger)).append('\n');
        builder.append("formActivity:" + String.valueOf(formActivity)).append('\n');
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(builder.toString());
    }
}
