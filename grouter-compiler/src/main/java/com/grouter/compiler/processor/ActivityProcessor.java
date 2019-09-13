package com.grouter.compiler.processor;

import com.grouter.RouterActivity;
import com.grouter.RouterField;
import com.grouter.compiler.ParameterModel;
import com.grouter.compiler.ActivityModel;
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

public class ActivityProcessor {
    private RouterProcessor routerProcessor;

    public ActivityProcessor(RouterProcessor routerProcessor) {
        this.routerProcessor = routerProcessor;
    }

    public List<ActivityModel> process(RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(RouterActivity.class);
        List<ActivityModel> typeModels = new ArrayList<>();
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            RouterActivity routerActivity = typeElement.getAnnotation(RouterActivity.class);
            ActivityModel typeModel = new ActivityModel();
            typeModels.add(typeModel);
            typeModel.exported = routerActivity.exported();
            typeModel.name = routerActivity.name();
            typeModel.description = routerActivity.description();
            typeModel.path = routerActivity.value();

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
                ParameterModel parameterModel = new ParameterModel();
                parameterModel.queryName = routerField.value();
                TypeName typeName = TypeName.get(mb.asType());
                parameterModel.rawType = typeName.toString();
                parameterModel.type = TypeUtils.getRouterActivityTypeString(typeName);
                parameterModel.name = mb.getSimpleName().toString();
                typeModel.params.add(parameterModel);
            }

        }
        Collections.sort(typeModels);
        return typeModels;
    }



}
