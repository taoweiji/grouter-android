package com.grouter;

import java.lang.reflect.Constructor;

@SuppressWarnings({"unchecked"})
public class ComponentUtils {
    public static <T> T getInstance(Class<T> protocol, String implementClass) {
        return getInstance(protocol, implementClass, null, null);
    }

    public static <T> T getInstance(Class<T> protocol, String implementClass, Class[] parameterTypes, Object[] parameterObjects) {
        ComponentRequest request = new ComponentRequest(protocol, implementClass, parameterTypes, parameterObjects);
        try {

            Object implement = InterceptorUtils.processComponent(request);
            if (request.isInterrupt()){
                return null;
            }
            if (implement != null) {
                return (T) implement;
            }
            Class clazz = Class.forName(request.getImplementClass());
            if (parameterTypes != null) {
                Constructor constructor = clazz.getConstructor(request.getParameterTypes());
                return (T) constructor.newInstance(request.getParameterObjects());
            } else {
                return (T) clazz.newInstance();
            }
        } catch (Exception e) {
            return (T) InterceptorUtils.getErrorDegradedComponent(request, e);
        }
    }
}
