package com.thejoyrun.androidrouter.demo;

import android.app.Activity;
import android.app.Application;

import com.thejoyrun.router.OtherRouterInitializer;
import com.thejoyrun.router.Router;
import com.thejoyrun.router.RouterInitializer;

import java.util.Map;

/**
 * Created by Wiki on 16/7/30.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Router.init("test");
        Router.setHttpHost("www.thejoyrun.com");
        Router.register(new RouterInitializer() {
            @Override
            public void init(Map<String, Class<? extends Activity>> router) {
                router.put("second2", SecondActivity.class);
                router.put("other://www.thejoyrun.com/second", SecondActivity.class);
            }
        });
        Router.register(new OtherRouterInitializer());
//        Router.setFilter(new Filter() {
//            @Override
//            public String doFilter(String url) {
//                return url.replace("test://www.thejoyrun.com/","test://");
//            }
//        });
    }
}
