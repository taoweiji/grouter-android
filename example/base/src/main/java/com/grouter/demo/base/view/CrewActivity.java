package com.grouter.demo.base.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.grouter.RouterActivity;
import com.grouter.demo.base.R;

@RouterActivity
public class CrewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crew);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//            GActivityCenter.ThirdActivity().uid(12).age(1).start(this);
//            GActivityCenter.SecondActivity().age(26).start(this);
//            GActivityCenter.KotlinActivity().start(this);
//            GActivityCenter.KotlinActivity().name("Wiki").start(this);
        });
//        GComponentCenter.getFeedServiceImpl();
    }

}
