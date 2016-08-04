package com.thejoyrun.router.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.thejoyrun.router.RouterActivity;
import com.thejoyrun.router.RouterField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {
    private Elements elementUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(RouterActivity.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("RouterProcessor");
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(RouterActivity.class);
        ClassName activityRouteTableInitializer = ClassName.get("com.thejoyrun.router", "ActivityRouteTableInitializer");
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder("AptActivityRouteTableInitializer")
                .addSuperinterface(activityRouteTableInitializer)
                .addModifiers(Modifier.PUBLIC)
                .addStaticBlock(CodeBlock.of("Routers.register(new AptActivityRouteTableInitializer());"));

        TypeElement activityRouteTableInitializertypeElement = elementUtils.getTypeElement(activityRouteTableInitializer.toString());
        List<? extends Element> members = elementUtils.getAllMembers(activityRouteTableInitializertypeElement);
        MethodSpec.Builder bindViewMethodSpecBuilder = null;
        for (Element element : members) {
            System.out.println(element.getSimpleName());
            if ("initRouterTable".equals(element.getSimpleName().toString())) {
                System.out.println("initRouterTable == ");
                bindViewMethodSpecBuilder = MethodSpec.overriding((ExecutableElement) element);
                break;
            }
        }
        if (bindViewMethodSpecBuilder == null) {
            return false;
        }
        ClassName activityHelperClassName = ClassName.get("com.thejoyrun.router", "ActivityHelper");

        List<MethodSpec> methodSpecs = new ArrayList<>();
        for (Element element : elements) {
            RouterActivity routerActivity = element.getAnnotation(RouterActivity.class);
            TypeElement typeElement = (TypeElement) element;
            for (String key : routerActivity.value()) {
                bindViewMethodSpecBuilder.addStatement("arg0.put($S, $T.class)", key, typeElement.asType());
            }
            ClassName className = buildActivityHelper(routerActivity.value()[0],activityHelperClassName, (TypeElement) element);

            MethodSpec methodSpec = MethodSpec.methodBuilder("get" + className.simpleName())
                    .addStatement("return new $T()",className)
                    .returns(className)
                    .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                    .build();
            methodSpecs.add(methodSpec);
        }
        TypeSpec typeSpecRoutersHelper = TypeSpec.classBuilder("RoutersHelper")
                .addModifiers(Modifier.PUBLIC)
                .addMethods(methodSpecs)
                .build();
        JavaFile javaFileRoutersHelper = JavaFile.builder("com.thejoyrun.router", typeSpecRoutersHelper).build();

        try {
            javaFileRoutersHelper.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }


        JavaFile javaFile = JavaFile.builder("com.thejoyrun.router", typeSpec.addMethod(bindViewMethodSpecBuilder.build()).build()).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    //TODO 支持在gradle关闭该功能
    private ClassName buildActivityHelper(String routerActivityName,ClassName activityHelperClassName, TypeElement typeElement) {
        List<? extends Element> members = elementUtils.getAllMembers(typeElement);
        List<MethodSpec> methodSpecs = new ArrayList<>();
        ClassName className = ClassName.get("com.thejoyrun.router",typeElement.getSimpleName() + "Helper");
        for (Element element : members) {
            RouterField routerField = element.getAnnotation(RouterField.class);
            if (routerField == null) {
                continue;
            }
            String name = element.getSimpleName().toString();
            if (name.length() >= 2 && name.charAt(0) == 'm' && Character.isUpperCase(name.charAt(1))){
                name = name.substring(1,2).toLowerCase() + name.substring(2);
            }
            String upperName = name.substring(0, 1).toUpperCase() + name.substring(1);
            MethodSpec methodSpec = MethodSpec.methodBuilder("with" + upperName)
                    .addParameter(TypeName.get(element.asType()), name)
                    .addStatement(String.format("put(\"%s\",%s )", routerField.value()[0], name))
                    .addStatement("return this")
                    .returns(className)
                    .addModifiers(Modifier.PUBLIC)
                    .build();
            methodSpecs.add(methodSpec);
        }
        MethodSpec methodSpec = MethodSpec.constructorBuilder()
                .addStatement("super($S)",routerActivityName)
                .addModifiers(Modifier.PUBLIC)
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder(typeElement.getSimpleName() + "Helper")
                .superclass(activityHelperClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethods(methodSpecs)
                .addMethod(methodSpec)
                .build();
        JavaFile javaFile = JavaFile.builder("com.thejoyrun.router", typeSpec).build();

        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return className;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }
}