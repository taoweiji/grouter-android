package com.grouter;


public class LoggerUtils {

    static void handleException(Exception e) {
        if (GRouterLogger.logger != null) {
            GRouterLogger.logger.handleException(e);
        }
    }

    static void onContinue(ComponentRequest request, Object implement) {
        if (GRouterLogger.logger != null) {
            GRouterLogger.logger.logger("grouter", "ComponentRequest.onContinue > " + request.getProtocol().getName() + " = " + implement.getClass().getName(), new Throwable());
        }
    }

    static void onContinue(ActivityRequest request, String activityClass) {
        if (GRouterLogger.logger != null) {
            GRouterLogger.logger.logger("grouter", "ActivityRequest.onContinue > " + activityClass, new Throwable());
        }
    }

    static void onContinue(TaskRequest request, GRouterTask task) {
        if (GRouterLogger.logger != null) {
            GRouterLogger.logger.logger("grouter", "TaskRequest.onContinue > " + task.getClass().getName(), new Throwable());
        }
    }

    static void onInterrupt(GRouterInterceptor.BaseRequest baseRequest, Exception e) {
        if (baseRequest instanceof ComponentRequest) {
            ComponentRequest request = (ComponentRequest) baseRequest;
            if (GRouterLogger.logger != null) {
                GRouterLogger.logger.logger("grouter", "ComponentRequest.onInterrupt > " + request.getProtocol().getName(), e);
            }
        } else if (baseRequest instanceof ActivityRequest) {
            ActivityRequest request = (ActivityRequest) baseRequest;
            if (GRouterLogger.logger != null) {
                GRouterLogger.logger.logger("grouter", "ActivityRequest.onInterrupt > " + request.getActivityClass(), e);
            }
        } else if (baseRequest instanceof TaskRequest) {
            TaskRequest request = (TaskRequest) baseRequest;
            if (GRouterLogger.logger != null) {
                GRouterLogger.logger.logger("grouter", "TaskRequest.onInterrupt > " + request.getTaskClass(), e);
            }
        }

    }

    static Exception getSerializationException() {
        return new Exception("Please add 'GRouter.setSerialization(serialization)' on Application.onCreate()");
    }

}
