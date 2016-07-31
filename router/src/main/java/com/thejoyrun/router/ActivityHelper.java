package com.thejoyrun.router;

import android.app.Activity;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ActivityHelper {
    protected final String host;

    public ActivityHelper(String host) {
        this.host = host;
    }

    protected Map<String, String> params = new HashMap<>();

    public String getUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(Routers.getScheme()).append("://").append(host);
        Set<String> keys = params.keySet();
        int i = 0;
        for (String key : keys) {
            if (i == 0) {
                builder.append('?');
            }
            builder.append(key).append('=').append(params.get(key));
            if (i < (keys.size() - 1)) {
                builder.append('&');
            }
            i++;
        }
        return builder.toString();
    }

    public void start(Context context) {
        Routers.startActivity(context, getUrl());
    }

    public void startForResult(Activity activity, int requestCode) {
        Routers.startActivityForResult(activity, getUrl(), requestCode);
    }

    public String put(String key, String value) {
        return params.put(key, value);
    }

    public String put(String key, double value) {
        return params.put(key, String.valueOf(value));
    }

    public String put(String key, float value) {
        return params.put(key, String.valueOf(value));
    }

    public String put(String key, int value) {
        return params.put(key, String.valueOf(value));
    }

    public String put(String key, boolean value) {
        return params.put(key, String.valueOf(value));
    }

}