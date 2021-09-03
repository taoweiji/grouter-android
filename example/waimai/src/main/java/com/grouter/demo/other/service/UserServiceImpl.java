package com.grouter.demo.other.service;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.grouter.RouterComponent;
import com.grouter.RouterComponentConstructor;
import com.grouter.demo.base.model.User;
import com.grouter.demo.base.service.UserService;

import java.util.Collections;
import java.util.List;

@RouterComponent(protocol = UserService.class, name = "用户服务",description = "本地或者服务端查询用户的信息")
public final class UserServiceImpl implements UserService {
    private User user;
    private int uid;
    private Context context;

    @RouterComponentConstructor
    public UserServiceImpl() {

    }
    @RouterComponentConstructor
    public UserServiceImpl(Context context) {
        super();
        this.context = context;
    }

//    public UserServiceImpl(User user, int uid) {
//        super();
//        this.user = user;
//        this.uid = uid;
//    }
//
//    public UserServiceImpl(User user, int uid, int[] sums) {
//        super();
//        this.user = user;
//        this.uid = uid;
//    }
//
//
//    @RouterComponentConstructor
//    public UserServiceImpl(List<User> users) {
//        super();
//    }
//
//    @RouterComponentConstructor
//    public UserServiceImpl(Map<String, User> users) {
//        super();
//    }
//
//    @RouterComponentConstructor
//    public UserServiceImpl(User[] users) {
//        super();
//    }


    public User getUser(int uid) {
        return new User("Wiki");
    }

    @NonNull
    public List<User> listUser() {
        return Collections.singletonList(new User("Wiki"));
    }

    @Override
    public void sayHello() {
        Log.e("UserId", String.valueOf(uid));
        if (user != null) {
            Log.e("User", user.getName());
        }
    }
}
