package com.grouter.compiler;

import java.util.ArrayList;
import java.util.List;

public class RouterModel {
    public List<ActivityModel> activityModels = new ArrayList<>();
    public List<ComponentModel> componentModels = new ArrayList<>();
    public List<InterceptorModel> interceptorModels = new ArrayList<>();
    public List<TaskModel> taskModels = new ArrayList<>();
    public List<RouterDelegateModel> delegateModels = new ArrayList<>();
    public String scheme;
    public String host;
    public String projectName;

    public void add(RouterModel routerModel) {
        activityModels.addAll(routerModel.activityModels);
        taskModels.addAll(routerModel.taskModels);
        interceptorModels.addAll(routerModel.interceptorModels);
        componentModels.addAll(routerModel.componentModels);
        delegateModels.addAll(routerModel.delegateModels);
    }
}
