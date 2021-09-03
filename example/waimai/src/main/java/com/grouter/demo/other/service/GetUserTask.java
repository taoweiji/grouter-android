package com.grouter.demo.other.service;

import android.util.Log;

import com.grouter.GRouterTask;
import com.grouter.RouterField;
import com.grouter.RouterTask;
import com.grouter.demo.base.model.User;

import java.util.Map;

@RouterTask(value = "getUser", returns = "User")
public class GetUserTask extends GRouterTask {

    @RouterField
    public int uid;

    @RouterField
    public String name;

    @Override
    public Object process() {
        Log.e("name", String.valueOf(name));
        Log.e("uid", String.valueOf(uid));
        return new User(uid, name);
    }
}

