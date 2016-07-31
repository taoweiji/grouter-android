package com.thejoyrun.router.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.thejoyrun.router.RouterActivity;

import java.io.IOException;
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

        for (Element element : elements) {
            RouterActivity routerActivity = element.getAnnotation(RouterActivity.class);
            TypeElement typeElement = (TypeElement) element;
            for (String key : routerActivity.value()) {
                bindViewMethodSpecBuilder.addStatement("arg0.put($S, $T.class)", key, typeElement.asType());
            }
        }
        JavaFile javaFile = JavaFile.builder("com.thejoyrun.router", typeSpec.addMethod(bindViewMethodSpecBuilder.build()).build()).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
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