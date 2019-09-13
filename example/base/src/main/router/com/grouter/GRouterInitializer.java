package com.grouter;

import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

@SuppressWarnings("unused")
public class GRouterInitializer extends GRouter {
  private static HashMap<String, String> activityMap = new HashMap<>();

  private static HashMap<String, String> componentMap = new HashMap<>();

  private static HashMap<String, String> taskMap = new HashMap<>();

  static {
    // app
    // 
    // 
    activityMap.put("cinema", "com.grouter.demo.activity.CinemaActivity");
    activityMap.put("cinema/detail", "com.grouter.demo.activity.CinemaDetailActivity");
    activityMap.put("moment", "com.grouter.demo.activity.MomentActivity");
    activityMap.put("moment/list", "com.grouter.demo.activity.MomentListActivity");
    activityMap.put("movie", "com.grouter.demo.activity.MovieActivity");
    activityMap.put("movie/photo", "com.grouter.demo.activity.MoviePhotoActivity");
    activityMap.put("movie/worker", "com.grouter.demo.activity.MovieWorkerActivity");
    activityMap.put("settings", "com.grouter.demo.activity.SettingsActivity");
    activityMap.put("settings/about_us", "com.grouter.demo.activity.AboutUsActivity");
    activityMap.put("about_us", "com.grouter.demo.activity.AboutUsActivity");
    activityMap.put("settings/network/ping", "com.grouter.demo.activity.NetworkPingActivity");
    activityMap.put("user", "com.grouter.demo.activity.UserActivity");
    // base
    // 
    // travel
    activityMap.put("travel/home", "com.grouter.example.travel.TravelHomeActivity");
    activityMap.put("travel/map", "com.grouter.example.travel.TravelMapActivity");
    activityMap.put("travel/poi", "com.grouter.example.travel.TravelPoiActivity");
    // waimai
    activityMap.put("account/bind", "com.grouter.demo.other.activity.AccountBindActivity");
    // webview
    activityMap.put("web_view", "com.grouter.demo.webview.activity.WebViewActivity");
  }
  static {
    taskMap.put("UserList", "com.grouter.demo.service.UserListTask");
    taskMap.put("UserLogin", "com.grouter.demo.other.service.UserLoginTask");
    taskMap.put("getUser", "com.grouter.demo.other.service.GetUserTask");
  }
  static {
    componentMap.put("com.grouter.demo.base.service.FeedService", "com.grouter.demo.service.FeedServiceImpl");
    componentMap.put("android.support.v4.app.Fragment", "com.grouter.demo.other.activity.UserFragment");
    componentMap.put("com.grouter.demo.base.service.AccountService", "com.grouter.demo.other.service.AccountServiceImpl");
    componentMap.put("com.grouter.demo.base.service.UserService", "com.grouter.demo.other.service.UserServiceImpl");
    componentMap.put("com.grouter.demo.service.WaimaiService", "com.grouter.demo.service.WaimaiServiceImpl");
  }

  public GRouterInitializer() {
    super("grouter","",activityMap,componentMap,taskMap);
    addInterceptor("com.grouter.demo.AppRouterInterceptor");
    addInterceptor("com.grouter.demo.LoginRouterInterceptor");
    addInterceptor("com.grouter.demo.other.OtherRouterInterceptor");
  }
}
