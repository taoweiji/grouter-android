package com.thejoyrun.router;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by Wiki on 16/7/28.
 */
public class SafeBundle {
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

    static void typeWarning(String key, Object value, String className,
                            Object defaultValue, ClassCastException e) {
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

    public Object get(String key) {
        return bundle.get(key);
    }

    public boolean containsKey(String key) {
        return bundle.containsKey(key) || (uri != null && uri.getQueryParameter(key) != null);
    }

    public <T extends Parcelable> T getParcelable(String key) {
        return bundle.getParcelable(key);
    }

    public Serializable getSerializable(String key) {
        return bundle.getSerializable(key);
    }
}
