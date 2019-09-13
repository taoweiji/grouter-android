package com.grouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

@SuppressWarnings("unused")
public class ActivityRequest extends GRouterInterceptor.BaseRequest {
    private GActivityBuilder activityBuilder;
    private Context context;
    private int requestCode;

    ActivityRequest(GActivityBuilder activityBuilder) {
        this.activityBuilder = activityBuilder;
    }

    public Uri getData() {
        return activityBuilder.data;
    }
    public void setData(Uri data){
        activityBuilder.data = data;
    }

    public Bundle getExtras() {
        return activityBuilder.extras;
    }

    public Object getParam(String key) {
        Object result = activityBuilder.extras.get(key);
        if (result != null) {
            return result;
        }
        if (activityBuilder.data != null) {
            return activityBuilder.data.getQueryParameter(key);
        }
        return null;
    }

    public String getActivityClass() {
        return activityBuilder.activityClass;
    }

    public boolean isActivity(Class<? extends Activity> cls) {
        return cls.getName().equals(activityBuilder.activityClass);
    }

    public Intent getIntent() {
        return activityBuilder.getIntent(context);
    }

    public Context getContext() {
        return context;
    }

    public void onContinue(Class<? extends Activity> activityClass) {
        LoggerUtils.onContinue(this, activityClass.getName());
        activityBuilder.activityClass = activityClass.getName();
        this.interrupt = false;
    }

    public void onContinue(GActivityBuilder activityBuilder) {
        LoggerUtils.onContinue(this, activityBuilder.activityClass);
        this.activityBuilder = activityBuilder;
        this.interrupt = false;
    }

    public void onInterrupt() {
        super.onInterrupt(new InterruptedException());
    }

    public int getRequestCode() {
        return requestCode;
    }

    void setContext(Context context) {
        this.context = context;
    }

    void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public GActivityBuilder getActivityBuilder() {
        return activityBuilder;
    }
}