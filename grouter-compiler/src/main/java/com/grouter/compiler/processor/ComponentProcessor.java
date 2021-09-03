package com.grouter.compiler.processor;

import com.grouter.RouterComponent;
import com.grouter.RouterComponentConstructor;
import com.grouter.compiler.ComponentModel;
import com.grouter.compiler.TypeUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/*
{
    "protocol":"com.grouter.demo.base.service.UserService",
    "implement":"com.grouter.demo.other.service.UserServiceImpl",
    "parameterClasses":[
        "Context",
        "int",
        "int[]"
    ],
    "parameterNames":[
        "context",
        "uid",
        "sums"
    ]
}
 */
public class ComponentProcessor {

    private String getTypeStringForClass(List<? extends AnnotationMirror> annotationMirrors){
        String value = null;
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            Map<? extends ExecutableElement, ? extends AnnotationValue> values = annotationMirror.getElementValues();
            Set<? extends Map.Entry<? extends ExecutableElement, ? extends AnnotationValue>> set = values.entrySet();
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> item : set) {
                if ("protocol()".equals(item.getKey().toString())) {
                    value = item.getValue().toString();
                    value = value.substring(0, value.length() - 6);
                }
            }
        }
        return value;
    }

    public List<ComponentModel> process(RoundEnvironment roundEnv) {
        List<ComponentModel> componentModelList = new ArrayList<>();
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(RouterComponent.class);

        for (Element element : elements) {
            RouterComponent routerComponent = element.getAnnotation(RouterComponent.class);
            TypeElement typeElement = (TypeElement) element;
            List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
            String value = getTypeStringForClass(annotationMirrors);
            if (value == null) {
                continue;
            }
            ClassName interfaceClass = ClassName.bestGuess(value);
            ClassName classNameImpl = (ClassName) ClassName.get(typeElement.asType());
            ComponentModel componentModel = new ComponentModel();
            componentModelList.add(componentModel);
            componentModel.path = routerComponent.value();
            componentModel.protocol = TypeUtils.reflectionName(interfaceClass);
            componentModel.implement = TypeUtils.reflectionName(classNameImpl);
            componentModel.description = routerComponent.description();
            componentModel.name = routerComponent.name();

            List<? extends Element> items = element.getEnclosedElements();
            boolean hasDefaultConstructor = false;
            int constructorCount = 0;
            for (Element item : items) {
                if (item instanceof ExecutableElement) {
                    ExecutableElement executableElement = (ExecutableElement) item;
                    if (!executableElement.getSimpleName().contentEquals("<init>")) {
                        continue;
                    }
                    constructorCount++;
                    if (executableElement.getParameters().size() == 0) {
                        hasDefaultConstructor = true;
                    }
                    if (executableElement.getAnnotation(RouterComponentConstructor.class) == null) {
                        continue;
                    }

                    List<? extends VariableElement> parameters = executableElement.getParameters();
                    ComponentModel.ConstructorBean constructorBean = new ComponentModel.ConstructorBean();
                    componentModel.constructors.add(constructorBean);

                    if (parameters.size() > 0) {
                        for (int i = 0; i < parameters.size(); i++) {
                            VariableElement variableElement = parameters.get(i);
                            TypeName typeName = TypeName.get(variableElement.asType());
                            constructorBean.parameterTypes.add(typeName.toString());
                            constructorBean.parameterNames.add(variableElement.getSimpleName().toString());
                        }
                    }
                }
            }
            if (constructorCount == 0){
                hasDefaultConstructor = true;
            }
            if (componentModel.constructors.size() == 0){
                if (hasDefaultConstructor){
                    ComponentModel.ConstructorBean constructorBean = new ComponentModel.ConstructorBean();
                    componentModel.constructors.add(constructorBean);
                }else {
                    // TODO 抛出异常（或者记录在日志中）
                }
            }
        }
        Collections.sort(componentModelList);
        return componentModelList;
    }


}