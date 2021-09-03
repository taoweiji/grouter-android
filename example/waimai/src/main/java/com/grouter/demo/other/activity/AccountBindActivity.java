package com.grouter.demo.other.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.grouter.GActivityCenter;
import com.grouter.RouterActivity;
import com.grouter.demo.waimai.R;


@RouterActivity("/account/bind")
public class AccountBindActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_module);
        GActivityCenter.AccountBindActivity();
    }

}
