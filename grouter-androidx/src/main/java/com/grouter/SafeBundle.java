package com.grouter;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Wiki on 19/7/28.
 */
@SuppressWarnings("WeakerAccess")
class SafeBundle {
    private static final String TAG = "SafeBundle";
    private Bundle bundle;
    private Uri uri;

    public SafeBundle(Bundle bundle, Uri uri) {
        this.bundle = bundle;
        this.uri = uri;
        if (this.bundle == null) {
            this.bundle = new Bundle();
        }
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        Object o = bundle.get(key);
        if (o == null && uri != null) {
            o = uri.getQueryParameter(key);
        }
        if (o == null) {
            return defaultValue;
        }
        return o.toString();
    }

    public double getDouble(String key, double defaultValue) {
        Object o = bundle.get(key);
        if (o == null && uri != null) {
            o = uri.getQueryParameter(key);
        }
        if (o == null) {
            return defaultValue;
        }
        try {
            if (o instanceof String) {
                return Double.parseDouble(o.toString());
            }
            return (Double) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Double", defaultValue, e);
            return defaultValue;
        }
    }


    public long getLong(String key, long defaultValue) {
        Object o = bundle.get(key);
        if (o == null && uri != null) {
            o = uri.getQueryParameter(key);
        }
        if (o == null) {
            return defaultValue;
        }
        try {
            if (o instanceof String) {
                return Long.parseLong(o.toString());
            }
            return (Long) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Long", defaultValue, e);
            return defaultValue;
        }
    }


    public int getInt(String key, int defaultValue) {
        Object o = bundle.get(key);
        if (o == null && uri != null) {
            o = uri.getQueryParameter(key);
        }
        if (o == null) {
            return defaultValue;
        }
        try {
            if (o instanceof String) {
                return Integer.parseInt(o.toString());
            }
            return (Integer) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Integer", defaultValue, e);
            return defaultValue;
        }
    }


    public boolean getBoolean(String key, boolean defaultValue) {
        Object o = bundle.get(key);
        if (o == null && uri != null) {
            o = uri.getQueryParameter(key);
        }
        if (o == null) {
            return defaultValue;
        }
        try {
            if (o instanceof String) {
                return Boolean.parseBoolean(o.toString());
            }
            return (Boolean) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Boolean", defaultValue, e);
            return defaultValue;
        }
    }


    public float getFloat(String key, float defaultValue) {
        Object o = bundle.get(key);
        if (o == null && uri != null) {
            o = uri.getQueryParameter(key);
        }
        if (o == null) {
            return defaultValue;
        }
        try {
            if (o instanceof String) {
                return Float.parseFloat(o.toString());
            }
            return (Float) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Float", defaultValue, e);
            return defaultValue;
        }
    }

    public int[] getIntArray(String key) {
        try {
            int[] array = bundle.getIntArray(key);
            if (array != null) {
                return array;
            }
            if (uri == null) {
                return null;
            }
            String text = uri.getQueryParameter(key);
            if (text == null || text.trim().length() == 0) {
                return null;
            }
            text = text.replace("[", "").replace("]", "");
            String[] split = text.split(",");
            int[] result = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                result[i] = Integer.parseInt(split[i].trim());
            }
            return result;
        } catch (Exception e) {
            Object object = bundle.get(key);
            if (object == null) {
                object = uri.getQueryParameter(key);
            }
            if (object != null) {
                typeWarning(key, object, "int[]", "null", e);
            } else {
                typeWarning(key, "null", "int[]", "null", e);
            }
        }
        return null;
    }

    public float[] getFloatArray(String key) {
        try {
            float[] array = bundle.getFloatArray(key);
            if (array != null) {
                return array;
            }
            if (uri == null) {
                return null;
            }
            String text = uri.getQueryParameter(key);
            if (text == null || text.trim().length() == 0) {
                return null;
            }
            text = text.replace("[", "").replace("]", "");
            String[] split = text.split(",");
            float[] result = new float[split.length];
            for (int i = 0; i < split.length; i++) {
                result[i] = Float.parseFloat(split[i].trim());
            }
            return result;
        } catch (Exception e) {
            Object object = bundle.get(key);
            if (object == null) {
                object = uri.getQueryParameter(key);
            }
            if (object != null) {
                typeWarning(key, object, "float[]", "null", e);
            } else {
                typeWarning(key, "null", "float[]", "null", e);
            }
        }
        return null;
    }

    public double[] getDoubleArray(String key) {

        try {
            double[] array = bundle.getDoubleArray(key);
            if (array != null) {
                return array;
            }
            if (uri == null) {
                return null;
            }
            String text = uri.getQueryParameter(key);
            if (text == null || text.trim().length() == 0) {
                return null;
            }
            text = text.replace("[", "").replace("]", "");
            String[] split = text.split(",");
            double[] result = new double[split.length];
            for (int i = 0; i < split.length; i++) {
                result[i] = Double.parseDouble(split[i].trim());
            }
            return result;
        } catch (Exception e) {
            Object object = bundle.get(key);
            if (object == null) {
                object = uri.getQueryParameter(key);
            }
            if (object != null) {
                typeWarning(key, object, "double[]", "null", e);
            } else {
                typeWarning(key, "null", "double[]", "null", e);
            }
        }
        return null;
    }

    public boolean[] getBooleanArray(String key) {
        try {
            boolean[] array = bundle.getBooleanArray(key);
            if (array != null) {
                return array;
            }
            if (uri == null) {
                return null;
            }
            String text = uri.getQueryParameter(key);
            if (text == null || text.trim().length() == 0) {
                return null;
            }
            text = text.replace("[", "").replace("]", "");
            String[] split = text.split(",");
            boolean[] result = new boolean[split.length];
            for (int i = 0; i < split.length; i++) {
                result[i] = Boolean.parseBoolean(split[i].trim());
            }
            return result;
        } catch (Exception e) {
            Object object = bundle.get(key);
            if (object == null) {
                object = uri.getQueryParameter(key);
            }
            if (object != null) {
                typeWarning(key, object, "boolean[]", "null", e);
            } else {
                typeWarning(key, "null", "boolean[]", "null", e);
            }
        }
        return null;
    }

    public long[] getLongArray(String key) {
        try {
            long[] array = bundle.getLongArray(key);
            if (array != null) {
                return array;
            }
            if (uri == null) {
                return null;
            }
            String text = uri.getQueryParameter(key);
            if (text == null || text.trim().length() == 0) {
                return null;
            }
            text = text.replace("[", "").replace("]", "");
            String[] split = text.split(",");
            long[] result = new long[split.length];
            for (int i = 0; i < split.length; i++) {
                result[i] = Long.parseLong(split[i].trim());
            }
            return result;
        } catch (Exception e) {
            Object object = bundle.get(key);
            if (object == null) {
                object = uri.getQueryParameter(key);
            }
            if (object != null) {
                typeWarning(key, object, "long[]", "null", e);
            } else {
                typeWarning(key, "null", "long[]", "null", e);
            }
        }
        return null;
    }


    static void typeWarning(String key, Object value, String className,
                            Object defaultValue, Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append("Key ");
        sb.append(key);
        sb.append(" expected ");
        sb.append(className);
        sb.append(" but value was a ");
        sb.append(value.getClass().getName());
        sb.append(".  The default value ");
        sb.append(defaultValue);
        sb.append(" was returned.");
        Log.w(TAG, sb.toString());
        Log.w(TAG, "Attempt to cast generated internal exception:", e);
    }

//    public Object get(String key) {
//        return bundle.get(key);
//    }

    public boolean containsKey(String key) {
        return bundle.containsKey(key) || (uri != null && uri.getQueryParameter(key) != null);
    }

//    public <T extends Parcelable> T getParcelable(String key) {
//        return bundle.getParcelable(key);
//    }

//    public Serializable getSerializable(String key) {
//        return bundle.getSerializable(key);
//    }
}
