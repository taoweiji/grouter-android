package com.grouter.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.lang.model.element.Modifier;

public class RouterComponentCodeBuilder {
    public static JavaFile getJavaFile(String centerName, List<ComponentModel> componentModels) {
        TypeSpec.Builder componentHelper = TypeSpec.classBuilder(centerName)
                .addModifiers(Modifier.PUBLIC);
        ClassName ComponentUtils = ClassName.bestGuess("com.grouter.ComponentUtils");

        for (ComponentModel component : componentModels) {
            ClassName protocol = ClassName.bestGuess(component.protocol);
            ClassName implement = ClassName.bestGuess(component.implement);


            for (ComponentModel.ConstructorBean constructor : component.constructors) {
                MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(implement.simpleName())
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(protocol);
                if (constructor.parameterTypes.size() > 0) {
                    methodSpec.addStatement("Class[] classes = new Class[$L]", constructor.parameterTypes.size());
                    methodSpec.addStatement("Object[] objects = new Object[$L]", constructor.parameterTypes.size());
                    for (int i = 0; i < constructor.parameterTypes.size(); i++) {
                        String clazz = constructor.parameterTypes.get(i);
                        String name = constructor.parameterNames.get(i);
                        TypeName typeName = TypeUtils.getTypeNameFull(clazz);
                        methodSpec.addParameter(typeName, name);
                        if (typeName instanceof ParameterizedTypeName) {
                            methodSpec.addStatement("classes[$L] = $T.class", i, ((ParameterizedTypeName) typeName).rawType);
                        } else {
                            methodSpec.addStatement("classes[$L] = $T.class", i, typeName);
                        }
                        methodSpec.addStatement("objects[$L] = $N", i, name);
                    }
                    methodSpec.addStatement("return $T.getInstance($T.class,$S,classes,objects)", ComponentUtils, protocol, TypeUtils.reflectionName(implement));
                } else {
                    methodSpec.addStatement("return $T.getInstance($T.class,$S)", ComponentUtils, protocol, TypeUtils.reflectionName(implement));
                }
                componentHelper.addMethod(methodSpec.build());
            }
        }
        AnnotationSpec annotationSpec = AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "\"unused\"").build();
        componentHelper.addAnnotation(annotationSpec);
        return JavaFile.builder("com.grouter", componentHelper.build()).build();
    }
}
