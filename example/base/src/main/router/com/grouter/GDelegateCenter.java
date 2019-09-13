package com.grouter;

import android.content.Context;
import com.grouter.demo.base.model.Moment;
import com.grouter.demo.base.model.User;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@SuppressWarnings({"unchecked","PrimitiveArrayArgumentToVarargsMethod"})
public class GDelegateCenter {
  public static MomentViewModelDelegate MomentViewModel(Context context) {
    return new MomentViewModelDelegate(context);
  }

  public static UserViewModelDelegate UserViewModel() {
    return new UserViewModelDelegate();
  }

  public static Account2ServiceDelegate Account2Service() {
    return new Account2ServiceDelegate();
  }

  public static Account2ServiceDelegate Account2Service(Context context, int uid, int[] uids, Map userMap, List users, User user, String[] names) {
    return new Account2ServiceDelegate(context,uid,uids,userMap,users,user,names);
  }

  public static Account2ServiceDelegate Account2Service(StringBuilder stringBuilder) {
    return new Account2ServiceDelegate(stringBuilder);
  }

  public static class MomentViewModelDelegate {
    private Class targetClass;

    private Object target;

    MomentViewModelDelegate(Context context) {
      try {
        targetClass = Class.forName("com.grouter.demo.delegate.MomentViewModel");
        Constructor<?> constructors = targetClass.getConstructor(Context.class);
        target = constructors.newInstance(context);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    /**
     * @param uid int
     * @return com.grouter.demo.base.model.Moment
     */
    public Moment getMoment(int uid) throws Exception {
      Method method = targetClass.getMethod("getMoment",int.class);
      return (Moment) method.invoke(target,uid);
    }

    /**
     * @param ids int[]
     * @return java.util.List<com.grouter.demo.base.model.Moment>
     */
    public List listMoment(int[] ids) throws Exception {
      Method method = targetClass.getMethod("listMoment",int[].class);
      return (List) method.invoke(target,ids);
    }

    /**
     * @param ids int[]
     * @return java.util.Vector<com.grouter.demo.base.model.Moment>
     */
    public Vector listMoment2(int[] ids) throws Exception {
      Method method = targetClass.getMethod("listMoment2",int[].class);
      return (Vector) method.invoke(target,ids);
    }

    /**
     * @param moment com.grouter.demo.base.model.Moment
     */
    public void delete(Moment moment) throws Exception {
      Method method = targetClass.getMethod("delete",Moment.class);
      method.invoke(target,moment);
    }
  }

  public static class UserViewModelDelegate {
    private Class targetClass;

    private Object target;

    UserViewModelDelegate() {
      try {
        targetClass = Class.forName("com.grouter.demo.delegate.UserViewModel");
        target = targetClass.newInstance();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    /**
     * @param uid int
     * @return com.grouter.demo.base.model.User
     */
    public User getUser(int uid) throws Exception {
      Method method = targetClass.getMethod("getUser",int.class);
      return (User) method.invoke(target,uid);
    }

    /**
     * @param name java.lang.String
     * @return com.grouter.demo.base.model.User
     */
    public User getUser(String name) throws Exception {
      Method method = targetClass.getMethod("getUser",String.class);
      return (User) method.invoke(target,name);
    }

    /**
     * @param uids int[]
     * @return java.util.List<com.grouter.demo.base.model.User>
     */
    public List listUser(int[] uids) throws Exception {
      Method method = targetClass.getMethod("listUser",int[].class);
      return (List) method.invoke(target,uids);
    }

    /**
     * @param user com.grouter.demo.base.model.User
     * @param stringBuilder java.lang.StringBuilder
     */
    public void delete(User user, StringBuilder stringBuilder) throws Exception {
      Method method = targetClass.getMethod("delete",User.class,StringBuilder.class);
      method.invoke(target,user,stringBuilder);
    }
  }

  public static class Account2ServiceDelegate {
    private Class targetClass;

    private Object target;

    Account2ServiceDelegate() {
      try {
        targetClass = Class.forName("com.grouter.demo.Account2Service");
        target = targetClass.newInstance();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    Account2ServiceDelegate(Context context, int uid, int[] uids, Map userMap, List users, User user, String[] names) {
      try {
        targetClass = Class.forName("com.grouter.demo.Account2Service");
        Constructor<?> constructors = targetClass.getConstructor(Context.class,int.class,int[].class,Map.class,List.class,User.class,String[].class);
        target = constructors.newInstance(context,uid,uids,userMap,users,user,names);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    Account2ServiceDelegate(StringBuilder stringBuilder) {
      try {
        targetClass = Class.forName("com.grouter.demo.Account2Service");
        Constructor<?> constructors = targetClass.getConstructor(StringBuilder.class);
        target = constructors.newInstance(stringBuilder);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    /**
     * @param uid java.lang.String
     * @param pwd java.lang.String
     * @return com.grouter.demo.base.model.User
     */
    public User login(String uid, String pwd) throws Exception {
      Method method = targetClass.getMethod("login",String.class,String.class);
      return (User) method.invoke(target,uid,pwd);
    }

    /**
     * @param context android.content.Context
     * @param map java.util.Map<java.lang.String, java.lang.String>
     * @param users java.util.List<com.grouter.demo.base.model.User>
     * @param a int[]
     * @param dad int
     */
    public void logout(Context context, Map map, List users, int[] a, int dad) throws Exception {
      Method method = targetClass.getMethod("logout",Context.class,Map.class,List.class,int[].class,int.class);
      method.invoke(target,context,map,users,a,dad);
    }

    public void showUser() throws Exception {
      Method method = targetClass.getMethod("showUser");
      method.invoke(target);
    }

    /**
     * @return int
     */
    public int showUser2() throws Exception {
      Method method = targetClass.getMethod("showUser2");
      return (int)method.invoke(target);
    }

    /**
     * @return com.grouter.demo.base.model.User
     */
    public User showUser3() throws Exception {
      Method method = targetClass.getMethod("showUser3");
      return (User)method.invoke(target);
    }

    /**
     * @return int[]
     */
    public int[] showUser4() throws Exception {
      Method method = targetClass.getMethod("showUser4");
      return (int[])method.invoke(target);
    }
  }
}
