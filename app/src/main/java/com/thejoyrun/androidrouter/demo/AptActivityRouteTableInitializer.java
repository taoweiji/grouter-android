//package com.thejoyrun.androidrouter.demo;
//
//import android.app.Activity;
//
//import com.thejoyrun.router.ActivityRouteTableInitializer;
//import com.thejoyrun.router.Routers;
//
//import java.util.Map;
//
///**
// * Created by Wiki on 16/7/30.
// */
//public class AptActivityRouteTableInitializer implements ActivityRouteTableInitializer {
//    static {
//        Routers.register(new AptActivityRouteTableInitializer());
//    }
//
//    @Override
//    public void initRouterTable(Map<String, Class<? extends Activity>> router) {
//        router.put("second", SecondActivity.class);
//    }
//}
