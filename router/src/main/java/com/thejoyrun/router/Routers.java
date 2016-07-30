package com.thejoyrun.router;

import android.app.Activity;

import java.lang.reflect.Field;

/**
 * Created by Wiki on 16/7/28.
 */
public class Routers {
    private static final String TAG = "Routers";

    private Routers(Activity activity) {
        activity.getIntent().getExtras();
    }

    public static void inject(Activity activity) {
        SafeBundle bundle = new SafeBundle(activity.getIntent().getExtras());
        Class clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        System.out.println(fields.length);
        for (Field field : fields) {
            RouterField annotation = field.getAnnotation(RouterField.class);
            if (annotation == null) {
                continue;
            }
            String type = field.getGenericType().toString();
            String name = annotation.value();
            field.setAccessible(true);
            try {
                if (!bundle.containsKey(name)) {
                    continue;
                }
                if (type.equals("double")) {
                    field.set(activity, bundle.getDouble(name, field.getDouble(activity)));
                    continue;
                } else if (type.equals("float")) {
                    field.set(activity, bundle.getFloat(name, field.getFloat(activity)));
                    continue;
                } else if (type.equals("int")) {
                    field.set(activity, bundle.getInt(name, field.getInt(activity)));
                    continue;
                } else if (type.equals("boolean")) {
                    field.set(activity, bundle.getBoolean(name, field.getBoolean(activity)));
                    continue;
                }
                Object defaultValue = field.get(activity);
                if (field.getGenericType() == String.class) {
                    field.set(activity, bundle.getString(name, (String) defaultValue));
                } else if (field.getGenericType() == Double.class) {
                    field.set(activity, bundle.getDouble(name, defaultValue != null ? (Double) defaultValue : 0));
                } else if (field.getGenericType() == Float.class) {
                    field.set(activity, bundle.getFloat(name, defaultValue != null ? (Float) defaultValue : 0));
                } else if (field.getGenericType() == Integer.class) {
                    field.set(activity, bundle.getInt(name, defaultValue != null ? (Integer) defaultValue : 0));
                } else if (field.getGenericType() == Boolean.class) {
                    field.set(activity, bundle.getBoolean(name, defaultValue != null ? (Boolean) defaultValue : false));
                }
//                else if (  Parcelable.class.isAssignableFrom(field.getGenericType())) {
//                    Object result = bundle.getParcelable(name);
//                    if (result != null) {
//                        field.set(activity, result);
//                    }
//                } else if (defaultValue instanceof Serializable) {
//                    Object result = bundle.getSerializable(name);
//                    if (result != null) {
//                        field.set(activity, result);
//                    }
//                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void open(String url) {

    }

}
