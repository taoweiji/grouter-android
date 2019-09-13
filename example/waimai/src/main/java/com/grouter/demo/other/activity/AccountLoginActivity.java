package com.grouter.demo.other.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.grouter.RouterActivity;
import com.grouter.demo.waimai.R;


//@RouterActivity("/account/login")
public class AccountLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_module);
    }

}
