package com.grouter.demo.delegate;

import com.alibaba.fastjson.JSON;
import com.grouter.delegate.UserViewModelDelegate;
import com.grouter.demo.base.model.User;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserViewModelTest {
    UserViewModelDelegate userViewModelDelegate;

    {
        try {
            userViewModelDelegate = new UserViewModelDelegate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUser() throws Exception {
        System.out.println(JSON.toJSONString(userViewModelDelegate.getUser(1),true));
    }

    @Test
    public void getUser1() throws Exception {
        System.out.println(JSON.toJSONString(userViewModelDelegate.getUser("Wiki"),true));
    }

    @Test
    public void listUser() throws Exception {
        System.out.println(JSON.toJSONString(userViewModelDelegate.listUser(new int[]{1,2,3,4}),true));
    }

    @Test
    public void delete() {
//        userViewModelDelegate.delete(new User());
    }
}