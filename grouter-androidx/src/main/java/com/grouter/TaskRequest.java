package com.grouter;

import android.net.Uri;
import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class TaskRequest extends GRouterInterceptor.BaseRequest {
    private GRouterTaskBuilder taskBuilder;
    private GRouterTask task;

    public TaskRequest(GRouterTaskBuilder taskBuilder) {
        this.taskBuilder = taskBuilder;
    }

    public String getTaskClass() {
        return taskBuilder.cls;
    }

    public Uri getUri() {
        return taskBuilder.data;
    }

    public String getPath() {
        if (getUri() != null && getUri().getPath() != null) {
            return getUri().getPath().substring(1);
        }
        return "";
    }

    public Map<String, Object> getParams() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.putAll(taskBuilder.params);
        objectMap.putAll(taskBuilder.uriParams);
        return objectMap;
    }

    GRouterTask getTask() {
        return task;
    }

    @Override
    public void onInterrupt(@NonNull Exception exception) {
        task = new ErrorTask(exception);
        super.onInterrupt(exception);
    }

    public void onContinue(@NonNull GRouterTask task) {
        this.task = task;
        this.interrupt = false;
        LoggerUtils.onContinue(this, task);
    }

    public void onContinueResult(@NonNull Object result) {
        onContinue(new MockTask(result));
    }

}