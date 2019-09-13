package com.grouter;

import android.net.Uri;
import android.os.Bundle;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "unused"})
public class GRouterTaskBuilder {
    String cls = "";
    Uri data;
    private GRouterTask task;
    Map<String, Object> params = new HashMap<>();
    Map<String, Object> uriParams = new HashMap<>();

    GRouterTaskBuilder(String cls) {
        if (cls != null) {
            this.cls = cls;
        }
    }

    GRouterTaskBuilder(String cls, Uri uri) {
        this(cls);
        this.data = uri;
        if (data != null) {
            Set<String> names = data.getQueryParameterNames();
            for (String name : names) {
                String value = data.getQueryParameter(name);
                if (value != null) {
                    uriParams.put(name, value);
                }
            }
        }
    }

    public GRouterTaskBuilder put(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public GRouterTask build() {
        if (task == null) {
            TaskRequest request = new TaskRequest(this);
            try {
                task = InterceptorUtils.processTask(request);
                if (task != null) {
                    inject(task);
                    return task;
                }
                Class<? extends GRouterTask> taskClass = (Class<? extends GRouterTask>) Class.forName(cls);
                task = taskClass.newInstance();
                inject(task);
            } catch (Exception e) {
                LoggerUtils.handleException(e);
                try {
                    task = InterceptorUtils.getErrorDegradedTask(request, e);
                    inject(task);
                } catch (Exception ex) {
                    LoggerUtils.handleException(ex);
                    task = new ErrorTask(ex);
                }
            }
        }
        return task;
    }

    private void inject(GRouterTask task) throws Exception {
        if (task instanceof MockTask || task instanceof ErrorTask) {
            return;
        }
        Set<Map.Entry<String, Object>> entrySet = params.entrySet();
        Class clazz = task.getClass();
        Field[] fields = clazz.getFields();
        for (Map.Entry<String, Object> item : entrySet) {
//            boolean isFound = false;
            for (Field field : fields) {
                if (field.getName().equals(item.getKey())) {
                    // 类型符合
                    if (field.getType().isAssignableFrom(item.getValue().getClass())) {
                        field.setAccessible(true);
                        field.set(task, item.getValue());
                    } else {
                        // 类型不符合，就当做URL参数赋值
                        if (data != null) {
                            data = data.buildUpon().appendQueryParameter(item.getKey(), item.getValue().toString()).build();
                        }
                    }
//                    isFound = true;
                    break;
                }
            }
//            if (!isFound) {
//                LoggerUtils.handleException(new NoSuchFieldException(task.getClass().getName() + "." + item.getKey()));
//            }
        }

        if (data != null) {
            RouterInject.inject(task, new Bundle(), data);
        }
        task.parameters.putAll(params);
        task.parameters.putAll(uriParams);
    }

    public GRouterTask.Response execute() {
        return build().execute();
    }

    public Object executeObject() throws Exception {
        return build().executeObject();
    }
}