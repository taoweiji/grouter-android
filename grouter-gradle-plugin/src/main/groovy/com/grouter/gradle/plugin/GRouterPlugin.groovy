package com.grouter.gradle.plugin

import com.alibaba.fastjson.JSON
import com.grouter.compiler.RouterBuildHelper
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin

//import com.android.build.gradle.api.BaseVariant

import router.ParserHelper


class GRouterPlugin implements Plugin<Project> {
    def GROUTER_SCHEME = 'router'
    def GROUTER_HOST = ''
    def GROUTER_SOURCE_PATH = ''

    def GROUTER_MULTI_PROJECT_MODE = false
    def GROUTER_MULTI_PROJECT_NAME = ""


    @Override
    void apply(Project project) {
        def ext = project.rootProject.property("ext")
        def OBJECT_GROUTER_MULTI_PROJECT_MODE = ext.properties.get("GROUTER_MULTI_PROJECT_MODE")
        GROUTER_MULTI_PROJECT_NAME = ext.properties.get("GROUTER_MULTI_PROJECT_NAME")
        GROUTER_SCHEME = ext.properties.get("GROUTER_SCHEME")
        GROUTER_HOST = ext.properties.get("GROUTER_HOST")
        GROUTER_SOURCE_PATH = ext.properties.get("GROUTER_SOURCE_PATH")

        def DEV_NOT_AUTO_DEPENDENCIES = ext.properties.get("DEV_NOT_AUTO_DEPENDENCIES")

        if (GROUTER_MULTI_PROJECT_NAME == null) {
            GROUTER_MULTI_PROJECT_MODE = false
        }
        if (OBJECT_GROUTER_MULTI_PROJECT_MODE != null) {
            GROUTER_MULTI_PROJECT_MODE = Boolean.valueOf(OBJECT_GROUTER_MULTI_PROJECT_MODE)
        }

        if (GROUTER_SOURCE_PATH == null || GROUTER_SOURCE_PATH.toString().length() == 0) {
            throw new RuntimeException("please in gradle.properties add GROUTER_SOURCE_PATH, e.g: GROUTER_SOURCE_PATH=app/src/main/java")
        }
        if (!GROUTER_MULTI_PROJECT_MODE) {
            GROUTER_MULTI_PROJECT_NAME = ""
        }
        if (GROUTER_HOST == null) {
            GROUTER_HOST = ""
        }
        if (GROUTER_SCHEME == null) {
            GROUTER_SCHEME = "grouter"
        }
        if (GROUTER_MULTI_PROJECT_NAME == null) {
            GROUTER_MULTI_PROJECT_NAME = ""
        }
//        def router = project.extensions.create('grouter', GRouterExtension)

        def hasApp = project.plugins.withType(AppPlugin)
        def hasLib = project.plugins.withType(LibraryPlugin)
        if (!hasApp && !hasLib) {
            throw new IllegalStateException("'android' or 'android-library' plugin required.")
        }
        def variants
        if (hasApp) {
            variants = project.android.applicationVariants
//            project.println("GRouter project.android.applicationVariants")
        } else {
            variants = project.android.libraryVariants
//            project.println("GRouter project.android.libraryVariants")
        }

        variants.all { variant ->
            try {
                String variantName = variant.name.capitalize()
                File sourceDir = new File(project.rootDir, GROUTER_SOURCE_PATH)
                File jsonDir = new File(sourceDir, "com/grouter/data")
                HashMap arguments = variant.getJavaCompileOptions().annotationProcessorOptions.arguments

                if (GROUTER_MULTI_PROJECT_NAME != null && !GROUTER_MULTI_PROJECT_NAME.isEmpty()) {
                    arguments.put("GROUTER_MULTI_PROJECT_NAME", GROUTER_MULTI_PROJECT_NAME)
                }
                if (GROUTER_SCHEME != null && !GROUTER_SCHEME.isEmpty()) {
                    arguments.put("GROUTER_SCHEME", GROUTER_SCHEME)
                }
                arguments.put("GROUTER_SOURCE_PATH", sourceDir.getAbsolutePath())
                arguments.put("MODULE_NAME", project.name)
                arguments.put("ROOT_PROJECT_DIR", project.rootDir.absolutePath)

//                def keySet = arguments.keySet()
//                for (def key : keySet) {
//                    project.println("$key = ${arguments[key]}")
//                }

                def task = project.tasks.create("GRouterProcessor" + variantName).doLast {

                }
//                if (kotlinMode) {
////                    task.dependsOn project.tasks["kapt${variantName}Kotlin"]
//                    task.dependsOn project.tasks["compile${variantName}Kotlin"]
//                } else {
//                    task.dependsOn project.tasks["extract${variantName}Annotations"]
//                }

                task.dependsOn project.tasks["compile${variantName}JavaWithJavac"]
//                task.dependsOn project.tasks["javaPreCompile${variantName}"]
                task.setGroup("grouter")


                def routerFixTask = project.tasks.create("GRouterFix" + variantName).doLast {
                    String projectName = "G"
                    if (GROUTER_MULTI_PROJECT_NAME != null && GROUTER_MULTI_PROJECT_NAME.length() > 0) {
                        projectName = GROUTER_MULTI_PROJECT_NAME
                    }
                    println("GRouter fix start")
                    def startTime = System.currentTimeMillis()
                    List<File> files = new ArrayList<>();
                    for (source in variant.sourceSets) {
                        files.addAll(source.getJavaDirectories())
                    }
                    if (!jsonDir.exists()) {
                        jsonDir.mkdirs()
                    }
                    ParserHelper.parse(jsonDir, files, project.name)
                    RouterBuildHelper.build(sourceDir, jsonDir, projectName, GROUTER_SCHEME, GROUTER_HOST)
                    println("GRouter fix end, duration: ${(System.currentTimeMillis() - startTime) / 1000} sec.")
                }
                routerFixTask.setGroup("grouter")
                routerFixTask.dependsOn project.tasks["generate${variantName}Sources"]
            } catch (Throwable e) {
                e.printStackTrace()
            }
        }
        // 生成文档插件
        def exportHTML = project.tasks.create("exportHTML").doFirst {
            def url = this.getClass().getResource("/META-INF/grouter.html")
            if (url != null) {
                try {
                    File sourceDir = new File(project.rootDir, GROUTER_SOURCE_PATH)
                    File jsonDir = new File(sourceDir, "com/grouter/data")
                    def routerModel = RouterBuildHelper.getRouterModel(jsonDir, project.name, GROUTER_SCHEME, GROUTER_HOST, true)
                    def activityModels = JSON.toJSONString(routerModel.activityModels, true)
                    def componentModels = JSON.toJSONString(routerModel.componentModels, true)
                    def taskModels = JSON.toJSONString(routerModel.taskModels, true)
                    def delegateModels = JSON.toJSONString(routerModel.delegateModels, true)
                    def is = this.getClass().getResourceAsStream("/META-INF/grouter.html")
                    def lines = is.readLines()
                    StringBuilder stringBuilder = new StringBuilder()
                    for (line in lines) {
                        stringBuilder.append(line).append('\n')
//                        println(line)
                    }
                    def htmlText = stringBuilder.toString()
                    htmlText = htmlText.replace("@ACTIVITY_MODELS", activityModels)
                    htmlText = htmlText.replace("@COMPONENT_MODELS", componentModels)
                    htmlText = htmlText.replace("@TASK_MODELS", taskModels)
                    htmlText = htmlText.replace("@DELEGATE_MODELS", delegateModels)
                    def outFile = new File(project.rootDir, "build/grouter.html")
                    outFile.write(htmlText)
                    println("生成HTML：file://" + outFile.absolutePath)
                } catch (Exception e) {
                    e.printStackTrace()
                }
            } else {
                println("找不到")
            }
        }
        exportHTML.setGroup("GRouter")


        def kotlinMode = project.plugins.hasPlugin("kotlin-android")
        // 如果是Kotlin项目，应该判断是否有 kotlin-kapt，如果没有就添加
        if (kotlinMode && !project.plugins.hasPlugin("kotlin-kapt")) {
            project.plugins.apply("kotlin-kapt")
            project.println("GRouter add project.plugins.apply(\"kotlin-kapt\")")
        }
        if (DEV_NOT_AUTO_DEPENDENCIES != null && DEV_NOT_AUTO_DEPENDENCIES.toString().toBoolean()) {
            println("开发模式，不自动增加依赖")
            return
        }
        def version = "1.2.1"
        project.dependencies {
            api "com.grouter:grouter:$version"
//                project.println("GRouter add api 'com.grouter:grouter:$version'")
            if (kotlinMode) {
                // 增加构造器
                kapt "com.grouter:grouter-compiler:$version"
//                project.println("GRouter add kapt 'com.grouter:grouter-compiler:$version'")
            } else {
                // Java 项目
                annotationProcessor "com.grouter:grouter-compiler:$version"
//                project.println("GRouter add annotationProcessor 'com.grouter:grouter-compiler:$version'")
            }
        }
    }
}
