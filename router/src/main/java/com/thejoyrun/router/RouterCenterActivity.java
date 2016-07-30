package com.thejoyrun.router;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Wiki on 16/7/31.
 */
public class RouterCenterActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getData() != null) {
            Routers.startActivity(this, getIntent().getDataString());
        }
        this.finish();
        Log.e("中央路由","RouterCenterActivity");
    }
}
