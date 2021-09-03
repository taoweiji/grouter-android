package com.grouter.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import static org.junit.Assert.*;

public class JavapoetTest {

    public static void main(String[] args) throws IOException {
        ClassName className = ClassName.bestGuess("com.taoweiji.User");

        TypeSpec.Builder userTypeSpec = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC).superclass(Serializable.class);

        // private int id = 0;
        FieldSpec idFieldSpec = FieldSpec.builder(int.class, "id", Modifier.PRIVATE)
                .initializer("0").build();
        userTypeSpec.addField(idFieldSpec);

        // private String name = "";
        FieldSpec nameFieldSpec = FieldSpec.builder(String.class, "name", Modifier.PRIVATE)
                .initializer("\"\"").build();
        userTypeSpec.addField(nameFieldSpec);

        //public User(int id, String name) {
        //    this.id = id;
        //    this.name = name;
        //}
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(int.class,"id")
                .addParameter(String.class,"name")
                .addStatement("this.id = id")
                .addStatement("this.name = name")
                .build();
        userTypeSpec.addMethod(constructor);

        //public void setId(int id) {
        //    this.id = id;
        //}
        MethodSpec setIdMethodSpec = MethodSpec.methodBuilder("setId")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(int.class,"id")
                .addStatement("this.id = id")
                .build();
        userTypeSpec.addMethod(setIdMethodSpec);

        //public int getId() {
        //    return id;
        //}
        MethodSpec getIdMethodSpec = MethodSpec.methodBuilder("getId")
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addStatement("return id")
                .build();
        userTypeSpec.addMethod(getIdMethodSpec);

        String text = "id: ,name: ";
        MethodSpec sayHiMethodSpec = MethodSpec.methodBuilder("sayHi")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$T text = \"id: $N,name: $N\"",String.class,"id","name")
                .addStatement("System.out.println(text)")
                .build();
        userTypeSpec.addMethod(sayHiMethodSpec);

        JavaFile javaFile = JavaFile.builder("com.taoweiji",userTypeSpec.build()).build();
        javaFile.writeTo(System.out);

        // javaFile.writeTo(new File("/tmp"));
    }
}