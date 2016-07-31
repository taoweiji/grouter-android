package com.thejoyrun.androidrouter.demo;

import android.app.Activity;
import android.app.Application;

import com.thejoyrun.router.ActivityRouteTableInitializer;
import com.thejoyrun.router.Filter;
import com.thejoyrun.router.Routers;

import java.util.Map;

/**
 * Created by Wiki on 16/7/30.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Routers.init("joyrun");
        Routers.setHttpHost("www.thejoyrun.com");
        Routers.register(new ActivityRouteTableInitializer() {
            @Override
            public void initRouterTable(Map<String, Class<? extends Activity>> router) {
                router.put("second2", SecondActivity.class);
                router.put("other://www.thejoyrun.com/second", SecondActivity.class);
            }
        });
        Routers.setFilter(new Filter() {
            @Override
            public String doFilter(String url) {
                return url.replace("joyrun://www.thejoyrun.com/","joyrun://");
            }
        });
    }
}
