package com.grouter;

public class InterceptorUtils {


    public static ActivityRequest processActivity(ActivityRequest request) {
        for (GRouterInterceptor interceptor : GRouter.getInstance().interceptors) {
            if (interceptor.process(request)) {
                break;
            }
        }
        return request;
    }

    public static GRouterTask processTask(TaskRequest request) {
        for (GRouterInterceptor interceptor : GRouter.getInstance().interceptors) {
            if (interceptor.process(request)) {
                break;
            }
        }
        return request.getTask();
    }
    public static Object processComponent(ComponentRequest request) {
        for (GRouterInterceptor interceptor : GRouter.getInstance().interceptors) {
            if (interceptor.process(request)) {
                break;
            }
        }
        return request.getImplement();
    }


    public static void onActivityError(ActivityRequest request, Exception exception) {
//        LoggerUtils.handleException(exception);
        request.onInterrupt(exception);
        for (GRouterInterceptor interceptor : GRouter.getInstance().interceptors) {
            if (interceptor.onError(request, exception)) {
                break;
            }
        }
        if (!request.isInterrupt()) {
            request.getContext().startActivity(request.getIntent());
        }
    }

    public static GRouterTask getErrorDegradedTask(TaskRequest request, Exception exception) {
//        LoggerUtils.handleException(exception);
        request.onInterrupt(exception);
        for (GRouterInterceptor interceptor : GRouter.getInstance().interceptors) {
            if (interceptor.onError(request, exception)) {
                break;
            }
        }
        return request.getTask();
    }



    public static Object getErrorDegradedComponent(ComponentRequest request, Exception exception) {
//        LoggerUtils.handleException(exception);
        request.onInterrupt(exception);
        for (GRouterInterceptor interceptor : GRouter.getInstance().interceptors) {
            if (interceptor.onError(request, exception)) {
                break;
            }
        }
        return request.getImplement();
    }
}
