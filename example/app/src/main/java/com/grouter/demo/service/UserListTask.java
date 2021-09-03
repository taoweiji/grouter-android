package com.grouter.demo.service;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.grouter.GRouterTask;
import com.grouter.RouterField;
import com.grouter.RouterTask;
import com.grouter.demo.base.model.User;

import java.util.List;
import java.util.Map;

@RouterTask(value = "UserList", returns = "List<User>")
public class UserListTask extends GRouterTask {

    @RouterField
    public String name;
    @RouterField
    public Context context;
    @RouterField
    public Activity activity;
    @RouterField
    public Application application;


    @Override
    public Object process() throws Exception {
        return null;
    }
}
