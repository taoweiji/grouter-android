package com.grouter.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Modifier;

public class RouterAutoInitializerBuilder {
    public static JavaFile getJavaFile(String projectName, String scheme, String host, RouterModel routerModel) {
        ClassName Router = ClassName.get("com.grouter", "GRouter");

        String initializerName = projectName + "RouterInitializer";

        TypeSpec.Builder RouterAutoInitializer = TypeSpec.classBuilder(initializerName)
                .addModifiers(Modifier.PUBLIC).superclass(Router);

        ClassName className = ClassName.get("java.util", "HashMap");
        ClassName stringTypeName = ClassName.get("java.lang", "String");
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(className, stringTypeName, stringTypeName);
        FieldSpec activityMap = FieldSpec.builder(parameterizedTypeName, "activityMap", Modifier.STATIC, Modifier.PRIVATE).initializer("new HashMap<>()").build();
        FieldSpec taskMap = FieldSpec.builder(parameterizedTypeName, "taskMap", Modifier.STATIC, Modifier.PRIVATE).initializer("new HashMap<>()").build();
        FieldSpec componentMap = FieldSpec.builder(parameterizedTypeName, "componentMap", Modifier.STATIC, Modifier.PRIVATE).initializer("new HashMap<>()").build();
        RouterAutoInitializer.addField(activityMap);
        RouterAutoInitializer.addField(componentMap);
        RouterAutoInitializer.addField(taskMap);
        MethodSpec.Builder methodSpec = MethodSpec.constructorBuilder().addStatement("super($S,$S,activityMap,componentMap,taskMap)", scheme, host).addModifiers(Modifier.PUBLIC);
        // 拦截器
        for (InterceptorModel item : routerModel.interceptorModels) {
            methodSpec.addStatement("addInterceptor($S)", item.type);
        }
        RouterAutoInitializer.addMethod(methodSpec.build());

        CodeBlock.Builder codeBlock = CodeBlock.builder();
        String currentModuleName = "";
        for (ActivityModel item : routerModel.activityModels) {
            if (!currentModuleName.equals(item.module)){
                currentModuleName = item.module;
                codeBlock.add("// $N\n",currentModuleName);
            }
            if (item.path != null && item.path.length() > 0) {
                String[] paths = item.path.split(",");
                for (String path : paths) {
                    if (path.startsWith("/")) {
                        path = path.substring(1);
                    }
                    codeBlock.addStatement("activityMap.put($S, $S)", path, item.type);
                }
            }else {
                codeBlock.add("// \n",item.type);
            }
        }
        RouterAutoInitializer.addStaticBlock(codeBlock.build());
        // component
        CodeBlock.Builder codeBlock2 = CodeBlock.builder();
        Set<String> set = new HashSet<>();
        for (ComponentModel componentModel : routerModel.componentModels) {
            if (!set.contains(componentModel.protocol)) {
                set.add(componentModel.protocol);
                codeBlock2.addStatement("componentMap.put($S, $S)", componentModel.protocol, componentModel.implement);
            }
            if (componentModel.path != null && componentModel.path.length() > 0) {
                String path = componentModel.path;
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }
                if (!set.contains(path)) {
                    set.add(path);
                    codeBlock2.addStatement("componentMap.put($S, $S)", path, componentModel.implement);
                }
            }
        }
        // task
        CodeBlock.Builder codeBlockTask = CodeBlock.builder();
        for (TaskModel item : routerModel.taskModels) {
            if (item.path != null && item.path.length() > 0) {
                String path = item.path;
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }
                codeBlockTask.addStatement("taskMap.put($S, $S)", path, item.type);
            } else {
                String simpleName = item.type.substring(item.type.lastIndexOf(".") + 1);
                codeBlockTask.addStatement("taskMap.put($S, $S)", simpleName, item.type);
            }
        }
        RouterAutoInitializer.addStaticBlock(codeBlockTask.build());
        RouterAutoInitializer.addStaticBlock(codeBlock2.build());

        // 增加 unused 注解

        AnnotationSpec annotationSpec = AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "\"unused\"").build();
        RouterAutoInitializer.addAnnotation(annotationSpec);

        return JavaFile.builder("com.grouter", RouterAutoInitializer.build()).build();
    }

}
