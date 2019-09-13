package com.grouter.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.lang.model.element.Modifier;

public class RouterActivityCodeBuilder {

    public static JavaFile getJavaFile(String centerName,List<ActivityModel> activityModels) {
        TypeSpec.Builder RouterHelper = TypeSpec.classBuilder(centerName)
                .addModifiers(Modifier.PUBLIC);
        TypeSpec.Builder BuilderSet = TypeSpec.classBuilder("BuilderSet")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        for (ActivityModel item : activityModels) {
            addTypeSpec(centerName,RouterHelper, BuilderSet, item);
        }
        RouterHelper.addType(BuilderSet.build());
        // 增加 unused 注解
        AnnotationSpec annotationSpec = AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "\"unused\"").build();
        RouterHelper.addAnnotation(annotationSpec);
        return JavaFile.builder("com.grouter", RouterHelper.build()).build();
    }

    private static ClassName RouterBuilder = ClassName.get("com.grouter", "GActivityBuilder");

    private static void addTypeSpec(String centerName,TypeSpec.Builder RouterHelper, TypeSpec.Builder builderSet, ActivityModel typeModel) {
        int index = typeModel.type.lastIndexOf(".") + 1;
        String name = typeModel.type.substring(index);
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(name + "Helper")
                .addModifiers(Modifier.STATIC,Modifier.PUBLIC).superclass(RouterBuilder);


        ClassName className = ClassName.get("com.grouter", centerName, "BuilderSet", name + "Helper");

        MethodSpec methodSpec = MethodSpec.constructorBuilder().addStatement("super($S)", typeModel.type).build();
        typeSpec.addMethod(methodSpec);
        for (ParameterModel typeMember : typeModel.params) {
            addMethodSpec(typeSpec, className, typeMember);
        }
        builderSet.addType(typeSpec.build());
        // 在 RouterHelper 增加快速创建构造器的方法
        MethodSpec methodSpec1 = MethodSpec.methodBuilder(name).addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(className).addStatement("return new $T()", className).build();
        RouterHelper.addMethod(methodSpec1);
    }

    private static void addMethodSpec(TypeSpec.Builder typeSpec, ClassName className, ParameterModel typeMember) {
        TypeName typeName = TypeUtils.getTypeName(typeMember.type);
        ParameterSpec parameterSpec = ParameterSpec.builder(typeName, typeMember.name).build();
        MethodSpec methodSpec = MethodSpec.methodBuilder(typeMember.name)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(parameterSpec)
                .addStatement("put($S,$N)", typeMember.name, typeMember.name)
                .addStatement("return this")
                .returns(className)
                .build();
        typeSpec.addMethod(methodSpec);
    }
}
