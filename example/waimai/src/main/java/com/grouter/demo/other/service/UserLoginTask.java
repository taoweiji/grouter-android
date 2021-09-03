package com.grouter.demo.other.service;

import android.hardware.SensorEvent;

import com.grouter.GRouterTask;
import com.grouter.RouterField;
import com.grouter.RouterTask;

import java.util.Map;

@RouterTask(value = "UserLogin",returns = "User")
public class UserLoginTask extends GRouterTask {
    @RouterField
    public int uid;

    @RouterField
    public String pwd;

    @RouterField
    public SensorEvent sensorEvent;


    @Override
    protected Object process() throws Exception {
        return null;
    }
}
