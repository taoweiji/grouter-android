package com.grouter.compiler.processor;

import com.google.auto.service.AutoService;
import com.grouter.RouterComponent;
import com.grouter.RouterInterceptor;
import com.grouter.RouterActivity;
import com.grouter.RouterTask;
import com.grouter.compiler.RouterBuildHelper;
import com.grouter.compiler.RouterModel;
import com.squareup.javapoet.TypeSpec;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {
    Elements elementUtils;
    private String MODULE_NAME = "";
    private String GROUTER_SOURCE_PATH = "";
    private String GROUTER_SCHEME = "";
    private String ROOT_PROJECT_DIR = "";
    //    String GROUTER_CENTER_NAME = "GActivityCenter";
//    String GCOMPONENT_CENTER_NAME = "GComponentCenter";
    private String GROUTER_MULTI_PROJECT_NAME = "";
    private Messager messagerUtils;


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(RouterActivity.class.getCanonicalName());
        set.add(RouterComponent.class.getCanonicalName());
        set.add(RouterInterceptor.class.getCanonicalName());
        set.add(RouterTask.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.size() == 0) {
            return false;
        }
        try {
            long startTime = System.currentTimeMillis();
            if (GROUTER_SOURCE_PATH.length() == 0) {
                throw new Exception("Grouter tips: com.android.tools.build:gradle  must >= 3.3.1, And setting android.enableSeparateAnnotationProcessing=true in the gradle.properties file.");
            }
            File sourceDir = new File(GROUTER_SOURCE_PATH);
            File jsonDir = new File(sourceDir, "com/grouter/data");
            if (!jsonDir.exists()) {
                jsonDir.mkdirs();
            }
            String projectName = "G";
            if (GROUTER_MULTI_PROJECT_NAME != null && GROUTER_MULTI_PROJECT_NAME.length() > 0) {
                projectName = GROUTER_MULTI_PROJECT_NAME;
            }
            RouterModel routerModel = new RouterModel();
            routerModel.activityModels = new ActivityProcessor(this).process(roundEnv);
            routerModel.componentModels = new ComponentProcessor().process(roundEnv);
            routerModel.interceptorModels = new InterceptorProcessor(this).process(roundEnv);
            routerModel.taskModels = new TaskProcessor(this).process(roundEnv);
            routerModel.delegateModels = new DelegateProcessor().process(roundEnv);
            RouterBuildHelper.saveRouterModel(jsonDir, routerModel, MODULE_NAME);
            RouterBuildHelper.build(sourceDir, jsonDir, projectName, GROUTER_SCHEME, "");
//            messagerUtils.printMessage(Diagnostic.Kind.NOTE, String.format("GRouter APT process, duration: %s sec.", (System.currentTimeMillis() - startTime) / 1000.0));
        } catch (Throwable e) {
            try {
                if (ROOT_PROJECT_DIR != null && ROOT_PROJECT_DIR.length() > 0) {
                    File logFile = new File(ROOT_PROJECT_DIR + "/build/grouter.log");
                    if (!logFile.getParentFile().exists()) {
                        logFile.getParentFile().mkdirs();
                    }
                    OutputStream os = new FileOutputStream(logFile, true);
                    PrintStream printStream = new PrintStream(os);
                    printStream.println();
                    printStream.println("--------");
                    printStream.println("MODULE_NAME = " + MODULE_NAME);
                    printStream.println("GROUTER_SCHEME = " + GROUTER_SCHEME);
                    printStream.println("GROUTER_SOURCE_PATH = " + GROUTER_SOURCE_PATH);
                    e.printStackTrace(printStream);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            e.printStackTrace(printStream);
            messagerUtils.printMessage(Diagnostic.Kind.ERROR, "GRouter APT process error: \n" + byteArrayOutputStream.toString());
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        this.messagerUtils = processingEnv.getMessager();
        Map<String, String> map = processingEnv.getOptions();
        Set<String> keys = map.keySet();

        for (String key : keys) {
            if ("MODULE_NAME".equals(key)) {
                this.MODULE_NAME = map.get(key);
            }
            if ("GROUTER_SOURCE_PATH".equals(key)) {
                this.GROUTER_SOURCE_PATH = map.get(key);
            }
            if ("GROUTER_SCHEME".equals(key)) {
                this.GROUTER_SCHEME = map.get(key);
            }
            if ("GROUTER_MULTI_PROJECT_NAME".equals(key)) {
                this.GROUTER_MULTI_PROJECT_NAME = map.get(key);
            }
            if ("ROOT_PROJECT_DIR".equals(key)) {
                this.ROOT_PROJECT_DIR = map.get(key);
            }
        }
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }


}