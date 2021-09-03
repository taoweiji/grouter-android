package com.grouter.compiler.processor;

import com.grouter.RouterDelegate;
import com.grouter.RouterDelegateConstructor;
import com.grouter.RouterDelegateMethod;
import com.grouter.compiler.RouterDelegateModel;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

public class DelegateProcessor {

    public DelegateProcessor() {
    }

    public List<RouterDelegateModel> process(RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(RouterDelegate.class);
        List<RouterDelegateModel> delegateModels = new ArrayList<>();
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            RouterDelegateModel delegateModel = new RouterDelegateModel();
            delegateModels.add(delegateModel);
            // 设置类名
            ClassName className = (ClassName) ClassName.get(typeElement.asType());
            delegateModel.type = className.packageName() + "." + className.simpleName();
            // 设置参数


            List<? extends Element> items = element.getEnclosedElements();
            boolean hasDefaultConstructor = false;
            int constructorCount = 0;

            for (Element item : items) {
                if (item instanceof ExecutableElement) {
                    ExecutableElement executableElement = (ExecutableElement) item;
                    // 处理构造方法
                    if (executableElement.getSimpleName().contentEquals("<init>")) {
                        List<? extends VariableElement> parameters = executableElement.getParameters();
                        if (parameters.size() == 0) {
                            hasDefaultConstructor = true;
                        }
                        constructorCount++;
                        RouterDelegateConstructor delegateConstructor = executableElement.getAnnotation(RouterDelegateConstructor.class);
                        // 如果是带有注解的构造方法，就直接添加
                        if (delegateConstructor != null) {
                            RouterDelegateModel.ConstructorBean constructorBean = new RouterDelegateModel.ConstructorBean();
                            delegateModel.constructor.add(constructorBean);
                            if (parameters.size() > 0) {
                                for (int i = 0; i < parameters.size(); i++) {
                                    VariableElement variableElement = parameters.get(i);
                                    TypeName typeName = TypeName.get(variableElement.asType());
                                    constructorBean.parameterTypes.add(typeName.toString());
                                    constructorBean.parameterNames.add(variableElement.getSimpleName().toString());
                                }
                            }
                        }
                    } else {
                        RouterDelegateMethod method = executableElement.getAnnotation(RouterDelegateMethod.class);
                        if (method == null) {
                            continue;
                        }
                        RouterDelegateModel.MethodsBean methodsBean = new RouterDelegateModel.MethodsBean();
                        delegateModel.methods.add(methodsBean);
                        methodsBean.name = item.getSimpleName().toString();
                        List<? extends VariableElement> parameters = executableElement.getParameters();
                        for (int i = 0; i < parameters.size(); i++) {
                            VariableElement variableElement = parameters.get(i);
                            TypeName typeName = TypeName.get(variableElement.asType());
                            methodsBean.parameterTypes.add(typeName.toString());
                            methodsBean.parameterNames.add(variableElement.getSimpleName().toString());
                        }
                        methodsBean.returns = executableElement.getReturnType().toString();
                    }
                }
            }
            // 如果构造方法的数量等于0，那么就代表存在构造方法
            if (constructorCount == 0) {
                hasDefaultConstructor = true;
            }
            // 如果没有带有注解的构造方法，那么就判断是否有默认的构造方法
            if (delegateModel.constructor.size() == 0 && hasDefaultConstructor) {
                RouterDelegateModel.ConstructorBean constructorBean = new RouterDelegateModel.ConstructorBean();
                delegateModel.constructor.add(constructorBean);
            }
        }
        return delegateModels;
    }
}
