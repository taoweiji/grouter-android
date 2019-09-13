package com.grouter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class RouterInject {
    private static List<Field> getDeclaredFields(Class clazz) {
        List<Field> fieldList = new ArrayList<>();
        for (; clazz != Object.class; clazz = clazz != null ? clazz.getSuperclass() : null) {
            Field[] fields = new Field[0];
            if (clazz != null) {
                fields = clazz.getDeclaredFields();
            }
            fieldList.addAll(Arrays.asList(fields));
        }
        return fieldList;
    }

    public static void inject(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        Bundle arguments = fragment.getArguments();
        if (arguments == null || arguments.size() == 0) {
            return;
        }
        try {
            inject(fragment, arguments, null);
        } catch (Exception e) {
            LoggerUtils.handleException(e);
        }
    }

    public static void inject(Activity activity) {
        Intent intent = activity.getIntent();
        inject(activity, intent);
    }

    /**
     * 通常是在 onNewIntent 调用，用于解析二级跳转
     */
    public static void inject(Activity activity, Intent intent) {
        try {
            inject(activity, intent.getExtras(), intent.getData());
        } catch (Exception e) {
            LoggerUtils.handleException(e);
        }
        // 解析二级跳转
        GActivityBuilder activityBuilder = getNextNav(intent);
        if (activityBuilder == null) {
            return;
        }
        activityBuilder.start(activity);
        // 删除二级跳转信息，避免重复跳转
        intent.removeExtra(GActivityBuilder.NEXT_NAV_ACTIVITY_BUILDER);
    }

    public static GActivityBuilder getNextNav(Intent intent) {
        if (!intent.hasExtra(GActivityBuilder.NEXT_NAV_ACTIVITY_BUILDER)) {
            return null;
        }
        return intent.getParcelableExtra(GActivityBuilder.NEXT_NAV_ACTIVITY_BUILDER);
    }


    public static void inject(Object targetObject, Bundle extras, Uri uri) throws Exception {
        SafeBundle bundle = new SafeBundle(extras, uri);
        Class clazz = targetObject.getClass();
        List<Field> fields = getDeclaredFields(clazz);
        for (Field field : fields) {
            RouterField annotation = field.getAnnotation(RouterField.class);
            if (annotation == null) {
                continue;
            }
            // 解析注解中的名称
            String name = annotation.value();
            if (name.length() != 0 && bundle.containsKey(name)) {
                assignment(targetObject, field, name, bundle);
            }
            // 解析字段名称
            else if (!name.equals(field.getName())) {
                assignment(targetObject, field, field.getName(), bundle);
            }
        }
    }

    private static void assignment(Object targetObject, Field field, String name, SafeBundle bundle) throws Exception {
        if (!bundle.containsKey(name)) {
            return;
        }
        Type type = field.getGenericType();
        field.setAccessible(true);
        Object defaultValue = field.get(targetObject);
        Object result = null;
        // 基本类型数组
        if (type == int[].class) {
            result = bundle.getIntArray(name);
        } else if (type == long[].class) {
            result = bundle.getLongArray(name);
        } else if (type == double[].class) {
            result = bundle.getDoubleArray(name);
        } else if (type == float[].class) {
            result = bundle.getFloatArray(name);
        } else if (type == boolean[].class) {
            result = bundle.getBooleanArray(name);
        }
        // 基本类型
        else if (type == int.class || type == Integer.class) {
            result = bundle.getInt(name, defaultValue != null ? (Integer) defaultValue : 0);
        } else if (type == long.class || type == Long.class) {
            result = bundle.getLong(name, defaultValue != null ? (Long) defaultValue : 0);
        } else if (type == double.class || type == Double.class) {
            result = bundle.getDouble(name, defaultValue != null ? (Double) defaultValue : 0);
        } else if (type == float.class || type == Float.class) {
            result = bundle.getFloat(name, defaultValue != null ? (Float) defaultValue : 0);
        } else if (type == boolean.class || type == Boolean.class) {
            result = bundle.getBoolean(name, defaultValue != null ? (Boolean) defaultValue : false);
        } else if (type == String.class) {
            result = bundle.getString(name);
        } else if (GRouter.getSerialization() != null) {
            String text = bundle.getString(name);
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                // 解析List列表
                if (parameterizedType.getRawType() == List.class) {
                    Type[] types = parameterizedType.getActualTypeArguments();
                    Class cls = (Class) types[0];
                    // 解析对象
                    if (!Serializable.class.isAssignableFrom(cls)) {
                        LoggerUtils.handleException(new NotSerializableException(cls.getName()));
                    }
                    result = GRouter.getSerialization().deserializeList(text, cls);
                } else {
                    // 解析对象
                    if (!Serializable.class.isAssignableFrom(field.getType())) {
                        LoggerUtils.handleException(new NotSerializableException(field.getType().getName()));
                    }
                    result = GRouter.getSerialization().deserializeObject(text, field.getType());
                }
            } else {
                // 解析对象
                if (!Serializable.class.isAssignableFrom(field.getType())) {
                    LoggerUtils.handleException(new NotSerializableException(field.getType().getName()));
                }
                result = GRouter.getSerialization().deserializeObject(text, field.getType());
            }
        }else {
            LoggerUtils.handleException(LoggerUtils.getSerializationException());
        }
        if (result != null) {
            field.set(targetObject, result);
        }
    }

//    static void setRouterField(Object targetObject, Field field, Object value) throws Exception {
//        if (value == null) {
//            return;
//        }
//        Type type = field.getGenericType();
//        field.setAccessible(true);
//        if (field.getType().isAssignableFrom(value.getClass())) {
//            field.set(targetObject, value);
//        }
//        String text = value.toString();
//        // 基本类型
//        if (type == int.class || type == Integer.class) {
//            field.setInt(targetObject, Integer.valueOf(text));
//        } else if (type == long.class || type == Long.class) {
//            field.setLong(targetObject, Long.valueOf(text));
//        } else if (type == double.class || type == Double.class) {
//            field.setDouble(targetObject, Double.valueOf(text));
//        } else if (type == float.class || type == Float.class) {
//            field.setFloat(targetObject, Float.valueOf(text));
//        } else if (type == boolean.class || type == Boolean.class) {
//            field.setBoolean(targetObject, Boolean.valueOf(text));
//        } else if (type == String.class) {
//            field.set(targetObject, text);
//        } else {
//            throw new Exception(text + " can not processActivity");
//        }
//    }
}
