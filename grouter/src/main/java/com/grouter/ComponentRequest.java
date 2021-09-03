package com.grouter;

import androidx.annotation.NonNull;

@SuppressWarnings("WeakerAccess")
public class ComponentRequest extends GRouterInterceptor.BaseRequest {
    private Class[] parameterTypes;
    private Object[] parameterObjects;
    private String implementClass;
    private Object implement;
    private Class protocol;

    public ComponentRequest(Class protocol, String implementClass, Class[] parameterTypes, Object[] parameterObjects) {
        this.protocol = protocol;
        this.implementClass = implementClass;
        this.parameterTypes = parameterTypes;
        this.parameterObjects = parameterObjects;
    }

    public void onContinue(@NonNull Object implement) {
        this.implement = implement;
        this.interrupt = false;
        LoggerUtils.onContinue(this, implement);
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getParameterObjects() {
        return parameterObjects;
    }

    public String getImplementClass() {
        return implementClass;
    }

    public Object getImplement() {
        return implement;
    }

    public Class getProtocol() {
        return protocol;
    }
}