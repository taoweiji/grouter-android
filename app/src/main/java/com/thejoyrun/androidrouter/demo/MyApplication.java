package com.thejoyrun.androidrouter.demo;

import android.app.Activity;
import android.app.Application;

import com.thejoyrun.router.ActivityRouteTableInitializer;
import com.thejoyrun.router.Router;

import java.util.Map;

/**
 * Created by Wiki on 16/7/30.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Router.init("joyrun");
        Router.setHttpHost("www.thejoyrun.com");
        Router.register(new ActivityRouteTableInitializer() {
            @Override
            public void initRouterTable(Map<String, Class<? extends Activity>> router) {
                router.put("second2", SecondActivity.class);
                router.put("other://www.thejoyrun.com/second", SecondActivity.class);
            }
        });
//        Router.setFilter(new Filter() {
//            @Override
//            public String doFilter(String url) {
//                return url.replace("joyrun://www.thejoyrun.com/","joyrun://");
//            }
//        });
    }
}
