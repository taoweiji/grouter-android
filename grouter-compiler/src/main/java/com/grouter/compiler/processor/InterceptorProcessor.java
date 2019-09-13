package com.grouter.compiler.processor;

import com.alibaba.fastjson.JSON;
import com.grouter.RouterInterceptor;
import com.grouter.compiler.InterceptorModel;
import com.grouter.compiler.TypeUtils;
import com.squareup.javapoet.ClassName;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class InterceptorProcessor {
    private RouterProcessor routerProcessor;

    public InterceptorProcessor(RouterProcessor routerProcessor) {
        this.routerProcessor = routerProcessor;
    }
    public List<InterceptorModel>  process(RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(RouterInterceptor.class);
        List<InterceptorModel> interceptorModels = new ArrayList<>();
        for (Element element : elements) {
            RouterInterceptor routerInterceptor = element.getAnnotation(RouterInterceptor.class);
            ClassName className = (ClassName) ClassName.get(element.asType());
            InterceptorModel interceptorModel = new InterceptorModel();
            interceptorModel.priority = routerInterceptor.priority();
            interceptorModel.type = TypeUtils.reflectionName(className);
            interceptorModels.add(interceptorModel);
        }
        Collections.sort(interceptorModels);
//        File dir = new File(routerProcessor.GROUTER_SOURCE_PATH);
//        try {
//            dir = new File(dir, "com/grouter");
//            File file = new File(dir, "RouterInterceptor-" + routerProcessor.MODULE_NAME + ".json");
//            FileWriter fileWriter = new FileWriter(file);
//            fileWriter.write(JSON.toJSONString(interceptorModels, true));
//            fileWriter.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return interceptorModels;
    }
}
