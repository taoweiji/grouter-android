package com.grouter.demo.delegate;

import com.alibaba.fastjson.JSON;
import com.grouter.GDelegateCenter;

import org.junit.Test;

public class UserViewModelTest {
    GDelegateCenter.UserViewModelDelegate userViewModelDelegate;

    {
        try {
            userViewModelDelegate = GDelegateCenter.UserViewModel();
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