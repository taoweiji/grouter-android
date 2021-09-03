package com.grouter.demo.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.grouter.GRouter;
import com.grouter.RouterActivity;
import com.grouter.RouterField;

@RouterActivity("moment")
public class MomentActivity extends AppCompatActivity {
    @RouterField
    public String mid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GRouter.inject(this);
        setTitle(getClass().getSimpleName());
    }

}
