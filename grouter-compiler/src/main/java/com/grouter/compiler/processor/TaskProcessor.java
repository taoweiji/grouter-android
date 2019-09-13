package com.grouter.compiler.processor;

import com.grouter.RouterField;
import com.grouter.RouterTask;
import com.grouter.compiler.ParameterModel;
import com.grouter.compiler.TaskModel;
import com.grouter.compiler.TypeUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;


public class TaskProcessor {
    private RouterProcessor routerProcessor;

    public TaskProcessor(RouterProcessor routerProcessor) {
        this.routerProcessor = routerProcessor;
    }
//    private String getTypeStringForClass(List<? extends AnnotationMirror> annotationMirrors){
//        String value = null;
//        for (AnnotationMirror annotationMirror : annotationMirrors) {
//            Map<? extends ExecutableElement, ? extends AnnotationValue> values = annotationMirror.getElementValues();
//            Set<? extends Map.Entry<? extends ExecutableElement, ? extends AnnotationValue>> set = values.entrySet();
//            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> item : set) {
//                if ("returns()".equals(item.getKey().toString())) {
//                    value = item.getValue().toString();
//                }
//            }
//        }
//        return value;
//    }

    public List<TaskModel> process(RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(RouterTask.class);
        List<TaskModel> typeModels = new ArrayList<>();
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            TaskModel typeModel = new TaskModel();
            RouterTask routerTask = element.getAnnotation(RouterTask.class);
            typeModel.returns = routerTask.returns();
            typeModel.name = routerTask.name();
            typeModel.description = routerTask.description();
            // 设置类名
            ClassName className = (ClassName) ClassName.get(typeElement.asType());
            typeModel.type = className.packageName() + "." + className.simpleName();
            // 设置参数
            List<? extends Element> members = routerProcessor.elementUtils.getAllMembers(typeElement);
            for (Element mb : members) {
                RouterField routerField = mb.getAnnotation(RouterField.class);
                if (routerField == null) {
                    continue;
                }
                if (!mb.getEnclosingElement().equals(typeElement)) {
                    System.out.println("父类属性");
                    continue;
                }
//                    System.out.println("getEnclosingElement:"+mb.getEnclosingElement().getSimpleName());
                ParameterModel parameterModel = new ParameterModel();
                parameterModel.queryName = routerField.value();
                TypeName typeName = TypeName.get(mb.asType());
                parameterModel.rawType = typeName.toString();
                parameterModel.type = TypeUtils.getRouterTaskTypeString(typeName);
                parameterModel.name = mb.getSimpleName().toString();
                typeModel.params.add(parameterModel);
            }
            // 设置路径
            RouterTask routerActivity = typeElement.getAnnotation(RouterTask.class);
            typeModel.path = routerActivity.value();
            typeModels.add(typeModel);
        }
        Collections.sort(typeModels);
//        String json = JSON.toJSONString(typeModels, true);
//
//        File dir = new File(routerProcessor.GROUTER_SOURCE_PATH);
//        if (!dir.exists()) {
//            dir.mkdir();
//        }
//        try {
//            dir = new File(dir, "com/grouter");
//            File file = new File(dir, "RouterTask-" + routerProcessor.MODULE_NAME + ".json");
//            FileWriter fileWriter = new FileWriter(file);
//            fileWriter.write(json);
//            fileWriter.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return typeModels;
    }
}