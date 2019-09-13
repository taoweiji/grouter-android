package com.grouter.demo.delegate;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.grouter.RouterDelegate;
import com.grouter.RouterDelegateConstructor;
import com.grouter.RouterDelegateMethod;
import com.grouter.demo.base.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@RouterDelegate
public class UserViewModel {

    private Context context;

//    @RouterDelegateConstructor
    public UserViewModel() {

    }
    public UserViewModel(Context context) {
        this.context = context;
    }

//    @RouterDelegateConstructor
    public UserViewModel(Context context, Map<String, String> map) {
        this.context = context;
    }

    public UserViewModel(Context context, int age) {
        this.context = context;
    }




    @RouterDelegateMethod
    public User getUser(int uid) {
        User user = new User();
        user.setUid(uid);
        return user;
    }

    @RouterDelegateMethod
    public User getUser(String name) {
        return new User(name);
    }


    @RouterDelegateMethod
    public List<User> listUser(int[] uids) {
        List<User> users = new ArrayList<>();
        for (int uid : uids) {
            User user = new User();
            user.setUid(uid);
            users.add(user);
        }
        return users;
    }
//    @RouterDelegateMethod
    public Vector<User> listUser2(int[] uids) {

        return null;
    }

    @RouterDelegateMethod
    public void delete(User user,StringBuilder stringBuilder) {
        System.out.println("删除成功-" + JSON.toJSONString(user));
    }





}
