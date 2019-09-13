package com.grouter.demo;

import com.alibaba.fastjson.JSON;

import org.junit.Test;

import java.util.List;

public class UserTest {

    @Test
    public void a(){
        String text = "[1,2,3,4,5,6,7,8,9,10]";
        List<Integer> result = JSON.parseArray(text,Integer.class).subList(0,5);
        System.out.println(JSON.toJSONString(result.toArray()));
    }
}