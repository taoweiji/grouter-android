package com.grouter.compiler;

import com.squareup.javapoet.CodeBlock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RouterDelegateCodeBuilderTest {

    @org.junit.Test
    public void buildJavaFile1() {

//        List<RouterDelegateModel> models = new ArrayList<>();
//        RouterDelegateModel model = new RouterDelegateModel();
//        model.type = "com.squareup.javapoet.CodeBlock";
//        RouterDelegateModel.ConstructorBean constructorBean = new RouterDelegateModel.ConstructorBean();
//        constructorBean.parameterTypes.add("com.squareup.javapoet.CodeBlock");
//        constructorBean.parameterNames.add("codeBlock");
//        model.constructor.add(constructorBean);
//        models.add(model);
//        System.out.println(new File("test").getAbsolutePath());
//
//        RouterDelegateCodeBuilder.buildJavaFile("","",models,new File("test"));
    }
    @org.junit.Test
    public void buildJavaFile() {
//        CodeBlock.Builder codeBlock = CodeBlock.builder();
//
////        codeBlock.addStatement()
//        codeBlock.beginControlFlow("try");
//        codeBlock.addStatement("targetClass = Class.forName($S)", "dddd");
//        codeBlock.addStatement("target = targetClass.newInstance()");
//        codeBlock.nextControlFlow(" catch (Exception e)");
//        codeBlock.addStatement("e.printStackTrace()");
//        codeBlock.endControlFlow();
////        codeBlock.beginControlFlow()
//        System.out.println(codeBlock.build().toString());

        File logFile = new File("/Users/Wiki/Documents/Workspace/joyrun/ActivityRouter"+"/build/grouter.log");
        if (!logFile.getParentFile().exists()){
            logFile.getParentFile().mkdirs();
        }
        try {
            OutputStream os = new FileOutputStream(logFile,true);
            PrintStream printStream = new PrintStream(os);
            new Throwable().printStackTrace(printStream);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}