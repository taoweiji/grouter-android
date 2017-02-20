package com.thejoyrun.router;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
        builder.append(Router.getScheme()).append("://").append(host);
        Set<String> keys = params.keySet();
        int i = 0;
        for (String key : keys) {
            String value = params.get(key);
            if (value == null){
                continue;
            }
            if (i == 0) {
                builder.append('?');
            }
            try {
                builder.append(key).append('=').append(URLEncoder.encode(value,"UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (i < (keys.size() - 1)) {
                builder.append('&');
            }
            i++;
        }
        return builder.toString();
    }

    public void start(Context context) {
        Router.startActivity(context, getUrl());
    }

    public void startForResult(Activity activity, int requestCode) {
        Router.startActivityForResult(activity, getUrl(), requestCode);
    }
    public void startForResult(Fragment fragment, int requestCode) {
        Router.startActivityForResult(fragment, getUrl(), requestCode);
    }
    public void startForResult(android.support.v4.app.Fragment fragment, int requestCode) {
        Router.startActivityForResult(fragment, getUrl(), requestCode);
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