package com.grouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wiki on 19/7/28.
 */
@SuppressWarnings({"unused", "UnusedReturnValue", "WeakerAccess"})
public abstract class GRouter {
    private Map<String, HashMap<String, String>> schemeActivityMap = new HashMap<>();
    private Map<String, HashMap<String, String>> schemeComponentMap = new HashMap<>();
    private Map<String, HashMap<String, String>> schemeTaskMap = new HashMap<>();

    private boolean isInit = false;
    private String scheme;
    /**
     * 仅仅作为URI占位使用，在匹配路径是没有任何意义，匹配路径只用到scheme
     */
    private String host;
    private HashMap<String, String> activityMap = new HashMap<>();
    private HashMap<String, String> componentMap = new HashMap<>();
    private HashMap<String, String> taskMap = new HashMap<>();
    List<GRouterInterceptor> interceptors = new ArrayList<>();
    private static GRouter instance;
//    private static boolean ignoreExported = false;

    /**
     * 获取GRouter实例，是的单Project项目会使用当前Project生成的GRouterInitializer，如果多Project就使用默认的。
     */
    public static GRouter getInstance() {
        if (instance == null) {
            try {
                Class clazz = Class.forName("com.grouter.GRouterInitializer");
                instance = (GRouter) clazz.newInstance();
            } catch (Exception e) {
                instance = new DefaultMultiProjectGRouter();
                Log.e("GRouter", "DefaultMultiProjectGRouter");
                LoggerUtils.handleException(e);
            }
        }
        return instance;
    }

    /**
     * 建议在 Application.Create() 进行初始化，可以多次调用
     *
     * @param context Application
     */
    public void init(@NonNull Context context, @Nullable String buildType, @Nullable Map<String, Object> params) {
        this.isInit = true;
        for (GRouterInterceptor interceptor : interceptors) {
            interceptor.init(context, buildType, params);
        }
    }

    public GRouter(String scheme, String host, HashMap<String, String> activityMap, HashMap<String, String> componentMap, HashMap<String, String> taskMap) {
        if (TextUtils.isEmpty(scheme)) {
            return;
        }
        this.scheme = scheme;
        this.host = host;
        this.schemeActivityMap.put(scheme, activityMap);
        this.schemeComponentMap.put(scheme, componentMap);
        this.schemeTaskMap.put(scheme, taskMap);
        this.activityMap = activityMap;
        this.componentMap = componentMap;
        this.taskMap = taskMap;
    }

    /**
     * 用于Activity 注入 RouterField 参数，可以独立使用
     */
    public static void inject(Activity activity) {
        RouterInject.inject(activity);
    }

    /**
     * 通常是在 onNewIntent 调用，用于解析二级跳转
     */
    public static void inject(Activity activity, Intent intent) {
        RouterInject.inject(activity, intent);
    }

    /**
     * 用于Fragment 注入 RouterField 参数，可以独立使用
     */
    public static void inject(Fragment fragment) {
        RouterInject.inject(fragment);
    }

    public static Serialization serialization;

    /**
     * 设置JSON序列化工具
     */
    public static void setSerialization(Serialization serialization) {
        GRouter.serialization = serialization;
    }

    static Serialization getSerialization() {
        return serialization;
    }

    /**
     * 添加拦截器
     */
    public void addInterceptor(GRouterInterceptor interceptor) {
        if (isInit) {
            LoggerUtils.handleException(new Exception("Please add 'addInterceptor()' before 'init()'"));
        }
        boolean addInterceptor = false;
        for (int i = 0; i < interceptors.size(); i++) {
            if (interceptor.priority() > interceptors.get(i).priority()) {
                interceptors.add(i, interceptor);
                addInterceptor = true;
                break;
            }
        }
        if (!addInterceptor) {
            interceptors.add(interceptor);
        }
    }

    void addInterceptor(String className) {
        try {
            Class<?> cls = Class.forName(className);
            addInterceptor((GRouterInterceptor) cls.newInstance());
        } catch (Exception e) {
            LoggerUtils.handleException(e);
        }
    }


    /**
     * 通过URL跳转页面。
     *
     * @param context Activity or Context
     * @param url     grouter://activity/user?uid=1
     * @return 是否成功跳转
     */
    public GActivityBuilder.Result startActivity(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            if (TextUtils.isEmpty(uri.getScheme())) {
                throw new URISyntaxException(url, "URL lack scheme（eg: grouter://xxx/xxx）");
            }
            return activityBuilder(uri).start(context);
        } catch (Exception e) {
            LoggerUtils.handleException(e);
        }
        return new GActivityBuilder.Result(false);
    }

    public GActivityBuilder.Result startActivity(Activity activity, String url, @Nullable Bundle options) {
        try {
            Uri uri = Uri.parse(url);
            if (TextUtils.isEmpty(uri.getScheme())) {
                throw new URISyntaxException(url, "URL lack scheme（eg: grouter://xxx/xxx）");
            }
            return activityBuilder(uri).start(activity, options);
        } catch (Exception e) {
            LoggerUtils.handleException(e);
        }
        return new GActivityBuilder.Result(false);
    }

    public GActivityBuilder.Result startActivity(Fragment fragment, String url) {
        return startActivity(fragment, url, null);
    }

    public GActivityBuilder.Result startActivity(Fragment fragment, String url, @Nullable Bundle options) {
        try {
            Uri uri = Uri.parse(url);
            if (TextUtils.isEmpty(uri.getScheme())) {
                throw new URISyntaxException(url, "URL lack scheme（eg: grouter://xxx/xxx）");
            }
            return activityBuilder(uri).start(fragment.getActivity());
        } catch (Exception e) {
            LoggerUtils.handleException(e);
        }
        return new GActivityBuilder.Result(false);
    }


    /**
     * 通过URL跳转页面。
     *
     * @param activity    Activity
     * @param url         grouter://activity/user?uid=1
     * @param requestCode requestCode > 0
     * @return 是否成功跳转
     */
    public GActivityBuilder.Result startActivityForResult(Activity activity, String url, int requestCode) {
        return startActivityForResult(activity, url, requestCode, null);
    }

    public GActivityBuilder.Result startActivityForResult(Activity activity, String url, int requestCode, @Nullable Bundle options) {
        try {
            Uri uri = Uri.parse(url);
            if (TextUtils.isEmpty(uri.getScheme())) {
                throw new URISyntaxException(url, "URL lack scheme（eg: grouter://xxx/xxx）");
            }
            return activityBuilder(uri).startForResult(activity, requestCode, options);
        } catch (Exception e) {
            LoggerUtils.handleException(e);
        }
        return new GActivityBuilder.Result(false);
    }

    /**
     * 通过URL跳转页面。
     *
     * @param fragment    Fragment
     * @param url         grouter://activity/user?uid=1
     * @param requestCode requestCode > 0
     * @return 是否成功跳转
     */
    public GActivityBuilder.Result startActivityForResult(Fragment fragment, String url, int requestCode) {
        return startActivityForResult(fragment, url, requestCode, null);
    }

    public GActivityBuilder.Result startActivityForResult(Fragment fragment, String url, int requestCode, @Nullable Bundle options) {
        try {
            Uri uri = Uri.parse(url);
            if (TextUtils.isEmpty(uri.getScheme())) {
                throw new URISyntaxException(url, "URL lack scheme（eg: grouter://xxx/xxx）");
            }
            return activityBuilder(uri).startForResult(fragment, requestCode, options);
        } catch (Exception e) {
            LoggerUtils.handleException(e);
        }
        return new GActivityBuilder.Result(false);
    }

    public String getHost() {
        return host;
    }

    public String getScheme() {
        return scheme;
    }


    /**
     * 适用于多Project单scheme项目，跨Project调用。如果是当前Project建议使用GActivityCenter
     */
    public GActivityBuilder activityBuilder(Uri uri) {
        String cls = getActivityClassString(uri);
        // 解析多级跳转
        String nextNavString = uri.getQueryParameter("nextNav");
        if (nextNavString != null && nextNavString.length() > 0) {
            try {
                Uri nextNavUri = Uri.parse(nextNavString);
                if (TextUtils.isEmpty(nextNavUri.getScheme())) {
                    throw new URISyntaxException(nextNavString, "URL lack scheme（eg: grouter://xxx/xxx）");
                }
                return new GActivityBuilder(cls, uri).nextNav(activityBuilder(nextNavUri));
            } catch (Exception e) {
                LoggerUtils.handleException(e);
            }
        }
        return new GActivityBuilder(cls, uri);
    }

    /**
     * 适用于多Project单scheme项目，跨Project调用。如果是当前Project建议使用GActivityCenter
     */
    public GActivityBuilder activityBuilder(String path) {
        String cls = getActivityClassString(path);
        if (cls != null) {
            return new GActivityBuilder(cls);
        }
        Uri uri = new Uri.Builder().scheme(scheme).authority("activity").path(path).build();
        return activityBuilder(uri);
    }

    /**
     * 适用于多Project多scheme项目，跨Project调用。如果是当前Project建议使用GActivityCenter
     */
    public GActivityBuilder activityBuilder(String scheme, String path) {
        Uri uri = new Uri.Builder().scheme(scheme).authority("activity").path(path).build();
        return activityBuilder(uri);
    }


    /**
     * 用于多Project项目，用于在Project注册其他的Project生成的GRouter
     *
     * @param project 其他关联的Project的生成类
     */
    public void registerMultiProject(GRouter project) {
        if (isInit) {
            throw new RuntimeException("registerMultiProject must be 'GRouter.getInstance().init(context)' before.");
        }
        if (!TextUtils.isEmpty(project.getScheme())) {
            HashMap<String, String> tmp = schemeActivityMap.get(project.getScheme());
            if (tmp == null) {
                schemeActivityMap.put(project.getScheme(), project.activityMap);
            } else {
                tmp.putAll(project.activityMap);
            }
        }
        if (!TextUtils.isEmpty(project.getScheme())) {
            HashMap<String, String> tmp = schemeComponentMap.get(project.getScheme());
            if (tmp == null) {
                schemeComponentMap.put(project.getScheme(), project.componentMap);
            } else {
                tmp.putAll(project.componentMap);
            }
        }
        if (!TextUtils.isEmpty(project.getScheme())) {
            HashMap<String, String> tmp = schemeTaskMap.get(project.getScheme());
            if (tmp == null) {
                schemeTaskMap.put(project.getScheme(), project.taskMap);
            } else {
                tmp.putAll(project.taskMap);
            }
        }
        if (schemeActivityMap.size() == 1) {
            this.activityMap.putAll(project.activityMap);
            this.componentMap.putAll(project.componentMap);
            this.taskMap.putAll(project.taskMap);
        }
        for (GRouterInterceptor interceptor : project.interceptors) {
            addInterceptor(interceptor);
        }
    }

    private String getActivityClassString(Uri uri) {
        HashMap<String, String> tmp = schemeActivityMap.get(uri.getScheme());
        if (tmp == null) {
            return null;
        }
        if (uri.getPath() != null && uri.getPath().length() > 0) {
            return tmp.get(uri.getPath().substring(1));
        }
        return null;
    }

    private String getTaskClassString(Uri uri) {
        HashMap<String, String> tmp = schemeTaskMap.get(uri.getScheme());
        if (tmp == null) {
            return null;
        }
        if (uri.getPath() != null && uri.getPath().length() > 0) {
            return tmp.get(uri.getPath().substring(1));
        }
        return null;
    }


    String getActivityClassString(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return activityMap.get(path);
    }

    public String getComponentClassString(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (schemeComponentMap.size() > 1 && path.contains("://")) {
            Uri uri = Uri.parse(path);
            HashMap<String, String> tmp = schemeComponentMap.get(uri.getScheme());
            if (tmp != null && uri.getPath() != null && uri.getPath().length() > 0) {
                String result = tmp.get(uri.getPath().substring(1));
                if (result != null) {
                    return result;
                }
            }
        }
        return componentMap.get(path);
    }

    private String getTaskClassString(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (schemeTaskMap.size() > 1 && path.contains("://")) {
            Uri uri = Uri.parse(path);
            HashMap<String, String> tmp = schemeTaskMap.get(uri.getScheme());
            if (tmp != null && uri.getPath() != null && uri.getPath().length() > 0) {
                String result = tmp.get(uri.getPath().substring(1));
                if (result != null) {
                    return result;
                }
            }
        }
        return taskMap.get(path);
    }


    /**
     * 用于RouterActivity传递对象参数的序列化和反序列化，
     * GRouter不提供JSON序列化和反序列化工具，提供接口给开发者，可以使用FastJson或者GSON等JSON库适配。
     */
    public interface Serialization {
        String serialize(Object object);

        <T> T deserializeObject(String json, Class<T> clazz);

        <T> List<T> deserializeList(String json, Class<T> clazz);
    }

    /**
     * 非下沉式组件间单Task服务获取，通过path获取Task实例。如果是当前Project建议使用GTaskCenter
     *
     * @param urlOrPath 如果是单scheme可以直接使用path访问，eg. GetUserInfo，
     *                  如果是多scheme跨Project访问需要scheme，eg. grouter://task/GetUserInfo or
     *                  或者带有参数请求，grouter://task/GetUserInfo?uid=1
     * @return Task实例
     */
    public GRouterTaskBuilder taskBuilder(String urlOrPath) {
        Uri uri;
        String clazzName;
        if (urlOrPath.contains("://")) {
            uri = Uri.parse(urlOrPath);
            clazzName = getTaskClassString(uri);
        } else {
            uri = new Uri.Builder().scheme(scheme).authority("task").path(urlOrPath).build();
            clazzName = getTaskClassString(urlOrPath);
        }
        return new GRouterTaskBuilder(clazzName, uri);
    }

    public GRouterTaskBuilder taskBuilder(String scheme, String path) {
        Uri uri = new Uri.Builder().scheme(scheme).authority("task").path(path).build();
        return taskBuilder(uri.toString());
    }

    /**
     * 通过路径获取Component
     */
    public Object getComponent(String path) {
        String clazzName = getComponentClassString(path);
        if (clazzName == null) {
            return null;
        }
        return ComponentUtils.getInstance(Object.class, clazzName, null, null);
    }

    /**
     * 传入协议名称
     */
    public <T> T getComponent(String path, Class<T> protocol) {
        String clazzName = getComponentClassString(path);
        if (clazzName == null) {
            return null;
        }
        return ComponentUtils.getInstance(protocol, clazzName, null, null);
    }
}
