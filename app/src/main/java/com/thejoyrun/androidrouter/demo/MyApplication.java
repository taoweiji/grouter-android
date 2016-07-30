package com.thejoyrun.androidrouter.demo;

import android.app.Application;

import com.thejoyrun.router.Routers;

/**
 * Created by Wiki on 16/7/30.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Routers.init("joyrun");
//        Routers.register(new ActivityRouteTableInitializer() {
//            @Override
//            public void initRouterTable(Map<String, Class<? extends Activity>> router) {
//                router.put("second", SecondActivity.class);
//            }
//        });
    }
}
