package com.thejoyrun.androidrouter.demo;

import java.io.Serializable;

/**
 * Created by Wiki on 16/7/30.
 */
public class User implements Serializable{
    private String name;

    public User(String name) {
        this.name = name;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
