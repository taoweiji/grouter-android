package com.grouter.demo.other;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.grouter.GRouterInterceptor;
import com.grouter.ActivityRequest;
import com.grouter.ComponentRequest;
import com.grouter.RouterInterceptor;
import com.grouter.TaskRequest;

import java.util.Map;

@RouterInterceptor
public class OtherRouterInterceptor extends GRouterInterceptor {
    @Override
    public boolean process(@NonNull TaskRequest request) {
        return super.process(request);
    }

    @Override
    public boolean process(@NonNull ActivityRequest request) {
        return super.process(request);
    }

    @Override
    public boolean process(@NonNull ComponentRequest request) {
        return super.process(request);
    }

    //    @Override
//    public void processActivity(InterceptorCallback interceptorCallback) {
//        interceptorCallback.onInterrupt(new Exception(""));
//    }

    @Override
    public void init(@NonNull Context context, String buildType, Map<String, Object> params) {
        Log.e("OtherRouterInterceptor","init");
    }
}
