package com.grouter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;

/**
 * TODO 允许链式访问，让访问继续。也允许直接消费掉拦截器，不允许下一个拦截器访问
 */
public abstract class GRouterInterceptor {

    protected void init(@NonNull Context context, @Nullable String buildType, @Nullable Map<String, Object> params) {

    }

    /**
     * 如果返回 true 那么就不会再检查下一个拦截器
     */
    public boolean process(@NonNull ActivityRequest request) {
        return false;
    }

    /**
     * 如果返回 true 那么就不会再检查下一个拦截器
     */
    public boolean process(@NonNull TaskRequest request) {
        return false;
    }

    /**
     * 如果返回 true 那么就不会再检查下一个拦截器
     */
    public boolean process(@NonNull ComponentRequest request) {
        return false;
    }

    /**
     * 如果返回 true 那么就不会再检查下一个拦截器
     */
    public boolean onError(@NonNull ActivityRequest request, @NonNull Exception exception) {
        return false;
    }

    /**
     * 如果返回 true 那么就不会再检查下一个拦截器
     */
    public boolean onError(@NonNull TaskRequest request, @NonNull Exception exception) {
        return false;
    }

    /**
     * 如果返回 true 那么就不会再检查下一个拦截器
     */
    public boolean onError(@NonNull ComponentRequest request, @NonNull Exception exception) {
        return false;
    }

    public static class BaseRequest {
        boolean interrupt;
        protected Exception exception;

        public void onInterrupt(@NonNull Exception exception) {
            this.interrupt = true;
            this.exception = exception;
            LoggerUtils.onInterrupt(this, exception);
        }

        public boolean isInterrupt() {
            return interrupt;
        }

    }

    int priority() {
        RouterInterceptor routerInterceptor = getClass().getAnnotation(RouterInterceptor.class);
        if (routerInterceptor == null) {
            return 0;
        }
        return routerInterceptor.priority();
    }
}
