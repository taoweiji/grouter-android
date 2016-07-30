package com.thejoyrun.androidrouter.demo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wiki on 16/7/30.
 */
public class RouterMap {
    private static final Map<String,Class> router = new HashMap<>();

    static {
        router.put("activity://second/:{name}", MainActivity.class);
        router.put("activity://third", MainActivity.class);
    }
    public static Class getActivity(String url){
        return null;
    }
}
