package com.thejoyrun.router;

import android.app.Activity;

import java.util.Map;

/**
 * Created by Wiki on 16/7/30.
 */
public interface ActivityRouteTableInitializer {

    void initRouterTable(Map<String, Class<? extends Activity>> router);
}
