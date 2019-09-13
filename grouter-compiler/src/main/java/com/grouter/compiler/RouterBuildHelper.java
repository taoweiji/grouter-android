package com.grouter.compiler;

import com.alibaba.fastjson.JSON;
import com.squareup.javapoet.JavaFile;

import java.io.File;
import java.util.Collections;

public class RouterBuildHelper {
    public static RouterModel getRouterModel(File jsonDir, String projectName, String scheme, String host, boolean html) {
        File[] files = jsonDir.listFiles();

        if (files == null) {
            return null;
        }
        RouterModel routerModel = new RouterModel();
        routerModel.scheme = scheme;
        routerModel.host = host;
        routerModel.projectName = projectName;

        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }
            String name = file.getName();
            if (name.startsWith("Router-") && name.endsWith(".json")) {
                String moduleName = name.replace("Router-", "").replace(".json", "");
                String json = FileUtils.readToString(file);
                if (json != null) {
                    RouterModel tmp = JSON.parseObject(json, RouterModel.class);
                    if (tmp != null) {
                        for (ActivityModel item : tmp.activityModels){
                            item.module = moduleName;
                        }
                        for (TaskModel item : tmp.taskModels){
                            item.module = moduleName;
                        }
                        for (ComponentModel item : tmp.componentModels){
                            item.module = moduleName;
                        }
                        for (RouterDelegateModel item : tmp.delegateModels){
                            item.module = moduleName;
                        }
                        routerModel.add(tmp);
                        if (html) {
                            parseHTMLInfo(moduleName, tmp);
                        }
                    }
                }
            }
        }
        Collections.sort(routerModel.activityModels);
        Collections.sort(routerModel.taskModels);
        Collections.sort(routerModel.interceptorModels);
        Collections.sort(routerModel.componentModels);
        Collections.sort(routerModel.delegateModels);
        return routerModel;
    }

    private static void parseHTMLInfo(String moduleName, RouterModel routerModel) {
        for (ActivityModel item : routerModel.activityModels) {
            item.module = moduleName;
            if (item.name == null || item.name.length() == 0) {
                item.name = item.type.substring(item.type.lastIndexOf(".") + 1);
            }
            StringBuilder stringBuilder = new StringBuilder();
            if (item.path != null && item.path.length() > 0) {
                stringBuilder.append(item.path);
            }else {
                item.exported = false;
            }
            if (item.params.size() > 0) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append('?');
                }
                for (ParameterModel parameter : item.params) {
                    stringBuilder.append(parameter.name).append('=').append('{').append(parameter.type).append('}').append('&');
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            item.argument = stringBuilder.toString();
        }
        for (TaskModel item : routerModel.taskModels) {
            item.module = moduleName;
            if (item.name == null || item.name.length() == 0) {
                item.name = item.type.substring(item.type.lastIndexOf(".") + 1);
            }
            StringBuilder stringBuilder = new StringBuilder();
            if (item.path != null && item.path.length() > 0) {
                stringBuilder.append(item.path);
            }
            if (item.params.size() > 0) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append('?');
                }
                for (ParameterModel parameter : item.params) {
                    stringBuilder.append(parameter.name).append('=').append('{').append(parameter.type).append('}').append('&');
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            item.argument = stringBuilder.toString();
        }
        for (InterceptorModel item : routerModel.interceptorModels) {
            item.module = moduleName;
        }
        for (RouterDelegateModel item : routerModel.delegateModels) {
            item.module = moduleName;
        }
        for (ComponentModel item : routerModel.componentModels) {
            item.module = moduleName;
            if (item.name == null || item.name.length() == 0) {
                item.name = item.protocol.substring(item.protocol.lastIndexOf(".") + 1);
            }
        }

    }


    public static void build(File sourceDir, File jsonDir, String projectName, String scheme, String host) {
        RouterModel routerModel = getRouterModel(jsonDir, projectName, scheme, host, false);
        if (routerModel == null) {
            return;
        }
        try {
            JavaFile activityJavaFile = RouterActivityCodeBuilder.getJavaFile(projectName + "ActivityCenter", routerModel.activityModels);
            JavaFile taskJavaFile = RouterTaskCodeBuilder.getJavaFile(projectName + "TaskCenter", routerModel.taskModels);
            JavaFile componentJavaFile = RouterComponentCodeBuilder.getJavaFile(projectName + "ComponentCenter", routerModel.componentModels);
            JavaFile initializer = RouterAutoInitializerBuilder.getJavaFile(projectName, scheme, host, routerModel);
            // 代理
            RouterDelegateCodeBuilder.buildJavaFile(projectName,projectName + "DelegateCenter", routerModel.delegateModels, sourceDir);

            activityJavaFile.writeTo(sourceDir);
            taskJavaFile.writeTo(sourceDir);
            componentJavaFile.writeTo(sourceDir);
            initializer.writeTo(sourceDir);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void saveRouterModel(File jsonDir, RouterModel routerModel, String moduleName) {
        FileUtils.write(new File(jsonDir, "Router-" + moduleName + ".json"), JSON.toJSONString(routerModel, true));
    }


}
