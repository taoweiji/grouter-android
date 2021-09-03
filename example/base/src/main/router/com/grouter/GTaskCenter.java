package com.grouter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.hardware.SensorEvent;
import java.lang.String;

public class GTaskCenter {
  public static BuilderSet.UserListTaskHelper UserListTask() {
    return new BuilderSet.UserListTaskHelper();
  }

  public static BuilderSet.UserLoginTaskHelper UserLoginTask() {
    return new BuilderSet.UserLoginTaskHelper();
  }

  public static BuilderSet.GetUserTaskHelper GetUserTask() {
    return new BuilderSet.GetUserTaskHelper();
  }

  public static class BuilderSet {
    public static class UserListTaskHelper extends GRouterTaskBuilder {
      UserListTaskHelper() {
        super("com.grouter.demo.service.UserListTask");
      }

      public UserListTaskHelper name(String name) {
        put("name",name);
        return this;
      }

      public UserListTaskHelper context(Context context) {
        put("context",context);
        return this;
      }

      public UserListTaskHelper activity(Activity activity) {
        put("activity",activity);
        return this;
      }

      public UserListTaskHelper application(Application application) {
        put("application",application);
        return this;
      }
    }

    public static class UserLoginTaskHelper extends GRouterTaskBuilder {
      UserLoginTaskHelper() {
        super("com.grouter.demo.other.service.UserLoginTask");
      }

      public UserLoginTaskHelper uid(int uid) {
        put("uid",uid);
        return this;
      }

      public UserLoginTaskHelper pwd(String pwd) {
        put("pwd",pwd);
        return this;
      }

      public UserLoginTaskHelper sensorEvent(SensorEvent sensorEvent) {
        put("sensorEvent",sensorEvent);
        return this;
      }
    }

    public static class GetUserTaskHelper extends GRouterTaskBuilder {
      GetUserTaskHelper() {
        super("com.grouter.demo.other.service.GetUserTask");
      }

      public GetUserTaskHelper uid(int uid) {
        put("uid",uid);
        return this;
      }

      public GetUserTaskHelper name(String name) {
        put("name",name);
        return this;
      }
    }
  }
}
