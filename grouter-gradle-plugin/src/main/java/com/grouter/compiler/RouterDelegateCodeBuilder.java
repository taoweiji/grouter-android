package com.grouter.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import javax.lang.model.element.Modifier;

public class RouterDelegateCodeBuilder {

    public static void buildJavaFile(String projectName, String centerName, List<RouterDelegateModel> delegateModels, File sourceDir) {
        TypeSpec.Builder taskCenter = TypeSpec.classBuilder(centerName)
                .addModifiers(Modifier.PUBLIC);
//        projectName = "Waimai";
//        String delegatePackageName = "com.grouter.delegate";
//        if (projectName.length() > 0 && !projectName.equals("G")) {
//            delegatePackageName = delegatePackageName + "." + projectName.toLowerCase();
//        }


        for (RouterDelegateModel delegateModel : delegateModels) {


            ClassName className = ClassName.bestGuess(delegateModel.type);
            TypeSpec.Builder typeSpec = TypeSpec.classBuilder(className.simpleName() + "Delegate").addModifiers(Modifier.PUBLIC);

            // 增加属性
//            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(Class.class));
            FieldSpec targetClass = FieldSpec.builder(ClassName.get(Class.class), "targetClass", Modifier.PRIVATE).build();
            FieldSpec target = FieldSpec.builder(ClassName.OBJECT, "target", Modifier.PRIVATE).build();
            typeSpec.addField(targetClass);
            typeSpec.addField(target);

            ClassName classNameDelegate = ClassName.get("com.grouter."+centerName, className.simpleName() + "Delegate");


            for (RouterDelegateModel.ConstructorBean constructorBean : delegateModel.constructor) {
                MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
                for (int i = 0; i < constructorBean.parameterTypes.size(); i++) {
                    String type = constructorBean.parameterTypes.get(i);
                    String name = constructorBean.parameterNames.get(i);
                    TypeName tmpClassName = TypeUtils.getTypeNameFull(type);
                    constructor.addParameter(tmpClassName, name);
                }
                StringBuilder parametersStringBuilder = new StringBuilder();
                if (constructorBean.parameterTypes.size() == 0) {
                    CodeBlock.Builder codeBlock = CodeBlock.builder();
                    codeBlock.beginControlFlow("try");
                    codeBlock.addStatement("targetClass = Class.forName($S)", delegateModel.type);
                    codeBlock.addStatement("target = targetClass.newInstance()");
                    codeBlock.nextControlFlow("catch (Exception e)");
                    codeBlock.addStatement("e.printStackTrace()");
                    codeBlock.endControlFlow();
                    constructor.addCode(codeBlock.build());
                } else {
                    CodeBlock.Builder codeBlock = CodeBlock.builder();
                    codeBlock.beginControlFlow("try");
                    codeBlock.addStatement("targetClass = Class.forName($S)", delegateModel.type);
                    // Constructor<?> constructors = targetClass.getConstructor(CodeBlock.class);
                    TypeName ConstructorType = ClassName.get(Constructor.class);
//                    CodeBlock.Builder getConstructorCodeBlock = CodeBlock.builder();
                    codeBlock.add("$T<?> constructors = targetClass.getConstructor(", ConstructorType);
                    for (int i = 0; i < constructorBean.parameterTypes.size(); i++) {
                        TypeName type = TypeUtils.getTypeNameFull(constructorBean.parameterTypes.get(i));
                        if (i == constructorBean.parameterTypes.size() - 1) {
                            codeBlock.add("$T.class);\n", type);
                        } else {
                            codeBlock.add("$T.class,", type);
                        }
                    }
//                    codeBlock.addStatement(getConstructorCodeBlock.build());
                    // target = constructors.newInstance(codeBlock);

                    for (String name : constructorBean.parameterNames) {
                        parametersStringBuilder.append(name).append(',');
                    }
                    parametersStringBuilder.deleteCharAt(parametersStringBuilder.length() - 1);
                    codeBlock.addStatement("target = constructors.newInstance(" + parametersStringBuilder.toString() + ")");


                    codeBlock.nextControlFlow("catch (Exception e)");
                    codeBlock.addStatement("e.printStackTrace()");
                    codeBlock.endControlFlow();
                    constructor.addCode(codeBlock.build());
                }
                MethodSpec constructorMethodSpec = constructor.build();
                typeSpec.addMethod(constructorMethodSpec);



                MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(className.simpleName())
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addParameters(constructorMethodSpec.parameters)
                        .addStatement("return new $T($N)", classNameDelegate, parametersStringBuilder)
                        .returns(classNameDelegate);
                taskCenter.addMethod(methodSpec.build());
            }
            for (RouterDelegateModel.MethodsBean methodsBean : delegateModel.methods) {
                MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodsBean.name).addModifiers(Modifier.PUBLIC).addException(Exception.class);
                for (int i = 0; i < methodsBean.parameterTypes.size(); i++) {
                    String type = methodsBean.parameterTypes.get(i);
                    String name = methodsBean.parameterNames.get(i);
                    TypeName tmpClassName = TypeUtils.getTypeNameFull(type);
                    methodBuilder.addParameter(tmpClassName, name);
                }
                TypeName returns = TypeUtils.getTypeNameFull(methodsBean.returns);
                methodBuilder.returns(returns);

                if (methodsBean.parameterTypes.size() == 0) {
                    CodeBlock.Builder codeBlock = CodeBlock.builder();
//                    codeBlock.beginControlFlow("try");
                    codeBlock.addStatement("$T method = targetClass.getMethod($S)", ClassName.get(Method.class), methodsBean.name);
                    if (returns != TypeName.VOID) {
                        methodBuilder.addJavadoc("@return $N\n", methodsBean.returns);
                        codeBlock.addStatement("return ($T)method.invoke(target)", returns);
                    } else {
                        codeBlock.addStatement("method.invoke(target)");
                    }
//                    codeBlock.nextControlFlow("catch (Exception e)");
//                    codeBlock.addStatement("e.printStackTrace()");
//                    codeBlock.endControlFlow();
//                    if (returns != TypeName.VOID) {
//                        if (returns.getClass().equals(TypeName.class)) {
//                            codeBlock.addStatement("return 0");
//                        } else {
//                            codeBlock.addStatement("return null");
//                        }
//                    }
                    methodBuilder.addCode(codeBlock.build());
                } else {
                    CodeBlock.Builder codeBlock = CodeBlock.builder();
//                    codeBlock.beginControlFlow("try");
                    codeBlock.add("$T method = targetClass.getMethod($S,", ClassName.get(Method.class), methodsBean.name);
                    for (int i = 0; i < methodsBean.parameterTypes.size(); i++) {
                        methodBuilder.addJavadoc("@param $N $N\n", methodsBean.parameterNames.get(i),methodsBean.parameterTypes.get(i));
                        TypeName type = TypeUtils.getTypeNameFull(methodsBean.parameterTypes.get(i));
                        if (i == methodsBean.parameterTypes.size() - 1) {
                            codeBlock.add("$T.class);\n", type);
                        } else {
                            codeBlock.add("$T.class,", type);
                        }
                    }

                    StringBuilder tmp = new StringBuilder();
                    for (String name : methodsBean.parameterNames) {
                        tmp.append(name).append(',');
                    }
                    tmp.deleteCharAt(tmp.length() - 1);
                    if (returns.equals(TypeName.VOID)) {
                        codeBlock.addStatement("method.invoke(target," + tmp.toString() + ")");
                    } else {
                        methodBuilder.addJavadoc("@return $N\n", methodsBean.returns);
                        codeBlock.addStatement("return ($T) method.invoke(target," + tmp.toString() + ")", returns);
                    }

//                    codeBlock.nextControlFlow("catch (Exception e)");
//                    codeBlock.addStatement("e.printStackTrace()");
//                    codeBlock.endControlFlow();
//                    if (returns != TypeName.VOID) {
//                        if (returns.getClass().equals(TypeName.class)) {
//                            codeBlock.addStatement("return 0");
//                        } else {
//                            codeBlock.addStatement("return null");
//                        }
//                    }
                    methodBuilder.addCode(codeBlock.build());
                }
                typeSpec.addMethod(methodBuilder.build());
            }
//            AnnotationSpec annotationSpec = AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "\"unchecked\"").build();
//            typeSpec.addAnnotation(annotationSpec);

            taskCenter.addType(typeSpec.addModifiers(Modifier.STATIC).build());
        }
        try {
            JavaFile taskCenterJavaFile = JavaFile.builder("com.grouter", taskCenter.build()).build();
            taskCenterJavaFile.writeTo(sourceDir);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
