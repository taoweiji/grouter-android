package com.thejoyrun.router;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Wiki on 16/7/31.
 */
public class RouterCenterActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri data = getIntent().getData();
        if (data != null) {
            String url = getIntent().getDataString();
            if (data.getScheme().equals("http") && !TextUtils.isEmpty(Routers.getHttpHost()) && Routers.getHttpHost().equals(data.getHost())) {
                url = url.replaceFirst("http", Routers.getScheme()).replace(Routers.getHttpHost() + "/", "");
            }
            Routers.startActivity(this, url);
        }
        this.finish();
        Log.e("中央路由", "RouterCenterActivity");
    }
}
