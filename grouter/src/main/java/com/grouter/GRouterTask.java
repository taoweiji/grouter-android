package com.grouter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * TODO 支持控制异步调用
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class GRouterTask {
    protected Map<String, Object> parameters = new HashMap<>();
    private Exception exception;
    public static ExecutorService executorService = new ScheduledThreadPoolExecutor(5);

    public interface Callback {
        void onResponse(Response response);
    }

    public final void enqueue(final Callback callback) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Response response = execute();
                if (callback != null) {
                    callback.onResponse(response);
                }
            }
        });
    }

    @NonNull
    public final Response execute() {
        Response response = new Response();
        try {
            response.value = executeObject();
        } catch (Exception e) {
//            LoggerUtils.taskInfo(e);
            response.exception = e;
        }
        return response;
    }

    @Nullable
    public final Object executeObject() throws Exception {
        if (exception != null) {
            throw exception;
        }
        return process();
    }

    protected abstract Object process() throws Exception;

    protected Map<String, Object> getParameters() {
        return parameters;
    }

    protected Object getParameter(String key) {
        return parameters.get(key);
    }

    public static class Response {
        private Exception exception;
        private Object value;
        private String text;
        private Map<String, Object> map;

        public Exception getException() {
            return exception;
        }

        @SuppressWarnings("unchecked")
        @Nullable
        public <T extends Serializable> T value(Class<T> clazz) {
            if (value == null) {
                return null;
            }
            if (clazz.isAssignableFrom(value.getClass())) {
                return (T) value;
            }
            try {
                if (GRouter.getSerialization() == null) {
                    throw LoggerUtils.getSerializationException();
                }
                if (!Serializable.class.isAssignableFrom(clazz)) {
                    LoggerUtils.handleException(new NotSerializableException(clazz.getName()));
                }
                return GRouter.getSerialization().deserializeObject(string(), clazz);
            } catch (Exception e) {
                LoggerUtils.handleException(e);
            }
            return null;
        }

        @Nullable
        @SuppressWarnings("unchecked")
        public <T extends Serializable> List<T> list(Class<T> clazz) {
            if (value == null) {
                return null;
            }
            try {
                if (List.class.isAssignableFrom(value.getClass())) {
                    List list = (List) value;
                    if (list.size() == 0) {
                        return list;
                    }
                    Object first = list.get(0);
                    if (clazz.isAssignableFrom(first.getClass())) {
                        return (List<T>) value;
                    }
                } else {
                    throw new UnsupportedOperationException("value is " + value.getClass().getName());
                }
                if (GRouter.getSerialization() == null) {
                    throw LoggerUtils.getSerializationException();
                }
                if (!Serializable.class.isAssignableFrom(clazz)) {
                    LoggerUtils.handleException(new NotSerializableException(clazz.getName()));
                }
                return GRouter.getSerialization().deserializeList(string(), clazz);
            } catch (Exception e) {
                LoggerUtils.handleException(e);
            }
            return null;
        }

        @Nullable
        public Object value() {
            return value;
        }

        @Nullable
        public String string() {
            if (text != null) {
                return text;
            }
            if (value == null) {
                return null;
            }
            if (value instanceof String) {
                text = (String) value;
                return text;
            }
            try {
                if (GRouter.getSerialization() == null) {
                    throw LoggerUtils.getSerializationException();
                }
                if (!Serializable.class.isAssignableFrom(value.getClass())) {
                    LoggerUtils.handleException(new NotSerializableException(value.getClass().getName()));
                }
                text = GRouter.getSerialization().serialize(value);
            } catch (Exception e) {
                LoggerUtils.handleException(e);
            }
            return text;
        }

        @NonNull
        public Map<String, Object> map() {
            if (map != null) {
                return map;
            }
            map = RouterUtils.objectToMap(value);
            return map;
        }

        public boolean isError() {
            return exception != null;
        }
    }
}

