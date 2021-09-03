package com.grouter;

import java.lang.Integer;
import java.lang.String;
import java.util.List;

public class GActivityCenter {
  public static BuilderSet.MainActivityHelper MainActivity() {
    return new BuilderSet.MainActivityHelper();
  }

  public static BuilderSet.LoginActivityHelper LoginActivity() {
    return new BuilderSet.LoginActivityHelper();
  }

  public static BuilderSet.OverridePendingTransitionActivityHelper OverridePendingTransitionActivity(
      ) {
    return new BuilderSet.OverridePendingTransitionActivityHelper();
  }

  public static BuilderSet.OverridePendingTransitionTargetActivityHelper OverridePendingTransitionTargetActivity(
      ) {
    return new BuilderSet.OverridePendingTransitionTargetActivityHelper();
  }

  public static BuilderSet.CinemaActivityHelper CinemaActivity() {
    return new BuilderSet.CinemaActivityHelper();
  }

  public static BuilderSet.CinemaDetailActivityHelper CinemaDetailActivity() {
    return new BuilderSet.CinemaDetailActivityHelper();
  }

  public static BuilderSet.MomentActivityHelper MomentActivity() {
    return new BuilderSet.MomentActivityHelper();
  }

  public static BuilderSet.MomentListActivityHelper MomentListActivity() {
    return new BuilderSet.MomentListActivityHelper();
  }

  public static BuilderSet.MovieActivityHelper MovieActivity() {
    return new BuilderSet.MovieActivityHelper();
  }

  public static BuilderSet.MoviePhotoActivityHelper MoviePhotoActivity() {
    return new BuilderSet.MoviePhotoActivityHelper();
  }

  public static BuilderSet.MovieWorkerActivityHelper MovieWorkerActivity() {
    return new BuilderSet.MovieWorkerActivityHelper();
  }

  public static BuilderSet.SettingsActivityHelper SettingsActivity() {
    return new BuilderSet.SettingsActivityHelper();
  }

  public static BuilderSet.AboutUsActivityHelper AboutUsActivity() {
    return new BuilderSet.AboutUsActivityHelper();
  }

  public static BuilderSet.NetworkPingActivityHelper NetworkPingActivity() {
    return new BuilderSet.NetworkPingActivityHelper();
  }

  public static BuilderSet.UserActivityHelper UserActivity() {
    return new BuilderSet.UserActivityHelper();
  }

  public static BuilderSet.CrewActivityHelper CrewActivity() {
    return new BuilderSet.CrewActivityHelper();
  }

  public static BuilderSet.TravelHomeActivityHelper TravelHomeActivity() {
    return new BuilderSet.TravelHomeActivityHelper();
  }

  public static BuilderSet.TravelMapActivityHelper TravelMapActivity() {
    return new BuilderSet.TravelMapActivityHelper();
  }

  public static BuilderSet.TravelPoiActivityHelper TravelPoiActivity() {
    return new BuilderSet.TravelPoiActivityHelper();
  }

  public static BuilderSet.AccountBindActivityHelper AccountBindActivity() {
    return new BuilderSet.AccountBindActivityHelper();
  }

  public static BuilderSet.WebViewActivityHelper WebViewActivity() {
    return new BuilderSet.WebViewActivityHelper();
  }

  public static class BuilderSet {
    public static class MainActivityHelper extends GActivityBuilder {
      MainActivityHelper() {
        super("com.grouter.demo.MainActivity");
      }
    }

    public static class LoginActivityHelper extends GActivityBuilder {
      LoginActivityHelper() {
        super("com.grouter.demo.activity.LoginActivity");
      }
    }

    public static class OverridePendingTransitionActivityHelper extends GActivityBuilder {
      OverridePendingTransitionActivityHelper() {
        super("com.grouter.demo.activity.OverridePendingTransitionActivity");
      }
    }

    public static class OverridePendingTransitionTargetActivityHelper extends GActivityBuilder {
      OverridePendingTransitionTargetActivityHelper() {
        super("com.grouter.demo.activity.OverridePendingTransitionTargetActivity");
      }
    }

    public static class CinemaActivityHelper extends GActivityBuilder {
      CinemaActivityHelper() {
        super("com.grouter.demo.activity.CinemaActivity");
      }

      public CinemaActivityHelper cinemaId(String cinemaId) {
        put("cinemaId",cinemaId);
        return this;
      }
    }

    public static class CinemaDetailActivityHelper extends GActivityBuilder {
      CinemaDetailActivityHelper() {
        super("com.grouter.demo.activity.CinemaDetailActivity");
      }

      public CinemaDetailActivityHelper cinemaId(String cinemaId) {
        put("cinemaId",cinemaId);
        return this;
      }
    }

    public static class MomentActivityHelper extends GActivityBuilder {
      MomentActivityHelper() {
        super("com.grouter.demo.activity.MomentActivity");
      }

      public MomentActivityHelper mid(String mid) {
        put("mid",mid);
        return this;
      }
    }

    public static class MomentListActivityHelper extends GActivityBuilder {
      MomentListActivityHelper() {
        super("com.grouter.demo.activity.MomentListActivity");
      }

      public MomentListActivityHelper mid(String mid) {
        put("mid",mid);
        return this;
      }
    }

    public static class MovieActivityHelper extends GActivityBuilder {
      MovieActivityHelper() {
        super("com.grouter.demo.activity.MovieActivity");
      }

      public MovieActivityHelper movieId(String movieId) {
        put("movieId",movieId);
        return this;
      }
    }

    public static class MoviePhotoActivityHelper extends GActivityBuilder {
      MoviePhotoActivityHelper() {
        super("com.grouter.demo.activity.MoviePhotoActivity");
      }

      public MoviePhotoActivityHelper movieId(String movieId) {
        put("movieId",movieId);
        return this;
      }
    }

    public static class MovieWorkerActivityHelper extends GActivityBuilder {
      MovieWorkerActivityHelper() {
        super("com.grouter.demo.activity.MovieWorkerActivity");
      }

      public MovieWorkerActivityHelper movieId(String movieId) {
        put("movieId",movieId);
        return this;
      }
    }

    public static class SettingsActivityHelper extends GActivityBuilder {
      SettingsActivityHelper() {
        super("com.grouter.demo.activity.SettingsActivity");
      }
    }

    public static class AboutUsActivityHelper extends GActivityBuilder {
      AboutUsActivityHelper() {
        super("com.grouter.demo.activity.AboutUsActivity");
      }
    }

    public static class NetworkPingActivityHelper extends GActivityBuilder {
      NetworkPingActivityHelper() {
        super("com.grouter.demo.activity.NetworkPingActivity");
      }
    }

    public static class UserActivityHelper extends GActivityBuilder {
      UserActivityHelper() {
        super("com.grouter.demo.activity.UserActivity");
      }

      public UserActivityHelper uid(Integer uid) {
        put("uid",uid);
        return this;
      }

      public UserActivityHelper name(String name) {
        put("name",name);
        return this;
      }

      public UserActivityHelper user(Object user) {
        put("user",user);
        return this;
      }

      public UserActivityHelper users(List users) {
        put("users",users);
        return this;
      }

      public UserActivityHelper uidArray(int[] uidArray) {
        put("uidArray",uidArray);
        return this;
      }
    }

    public static class CrewActivityHelper extends GActivityBuilder {
      CrewActivityHelper() {
        super("com.grouter.demo.base.view.CrewActivity");
      }
    }

    public static class TravelHomeActivityHelper extends GActivityBuilder {
      TravelHomeActivityHelper() {
        super("com.grouter.example.travel.TravelHomeActivity");
      }
    }

    public static class TravelMapActivityHelper extends GActivityBuilder {
      TravelMapActivityHelper() {
        super("com.grouter.example.travel.TravelMapActivity");
      }
    }

    public static class TravelPoiActivityHelper extends GActivityBuilder {
      TravelPoiActivityHelper() {
        super("com.grouter.example.travel.TravelPoiActivity");
      }
    }

    public static class AccountBindActivityHelper extends GActivityBuilder {
      AccountBindActivityHelper() {
        super("com.grouter.demo.other.activity.AccountBindActivity");
      }
    }

    public static class WebViewActivityHelper extends GActivityBuilder {
      WebViewActivityHelper() {
        super("com.grouter.demo.webview.activity.WebViewActivity");
      }

      public WebViewActivityHelper url(String url) {
        put("url",url);
        return this;
      }
    }
  }
}
