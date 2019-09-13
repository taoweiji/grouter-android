package com.grouter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import com.grouter.demo.base.service.AccountService;
import com.grouter.demo.base.service.FeedService;
import com.grouter.demo.base.service.UserService;
import com.grouter.demo.service.WaimaiService;

public class GComponentCenter {
  public static FeedService FeedServiceImpl() {
    return ComponentUtils.getInstance(FeedService.class,"com.grouter.demo.service.FeedServiceImpl");
  }

  public static FeedService FeedServiceImpl(Context context, int uid, int[] sums) {
    Class[] classes = new Class[3];
    Object[] objects = new Object[3];
    classes[0] = Context.class;
    objects[0] = context;
    classes[1] = int.class;
    objects[1] = uid;
    classes[2] = int[].class;
    objects[2] = sums;
    return ComponentUtils.getInstance(FeedService.class,"com.grouter.demo.service.FeedServiceImpl",classes,objects);
  }

  public static FeedService FeedServiceImpl(Activity activity) {
    Class[] classes = new Class[1];
    Object[] objects = new Object[1];
    classes[0] = Activity.class;
    objects[0] = activity;
    return ComponentUtils.getInstance(FeedService.class,"com.grouter.demo.service.FeedServiceImpl",classes,objects);
  }

  public static Fragment UserFragment() {
    return ComponentUtils.getInstance(Fragment.class,"com.grouter.demo.other.activity.UserFragment");
  }

  public static AccountService AccountServiceImpl() {
    return ComponentUtils.getInstance(AccountService.class,"com.grouter.demo.other.service.AccountServiceImpl");
  }

  public static UserService UserServiceImpl() {
    return ComponentUtils.getInstance(UserService.class,"com.grouter.demo.other.service.UserServiceImpl");
  }

  public static UserService UserServiceImpl(Context context) {
    Class[] classes = new Class[1];
    Object[] objects = new Object[1];
    classes[0] = Context.class;
    objects[0] = context;
    return ComponentUtils.getInstance(UserService.class,"com.grouter.demo.other.service.UserServiceImpl",classes,objects);
  }

  public static WaimaiService WaimaiServiceImpl() {
    return ComponentUtils.getInstance(WaimaiService.class,"com.grouter.demo.service.WaimaiServiceImpl");
  }
}
