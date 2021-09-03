package com.grouter.demo.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.grouter.GRouter;
import com.grouter.RouterActivity;

@RouterActivity
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GRouter.inject(this);
        setTitle(getClass().getSimpleName());
    }
}
