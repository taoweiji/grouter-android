package com.grouter.gradle.plugin

public class GRouterExtension {
    public boolean kotlinEnable = true

    // --- 基本参数 ---
    // 必填，填写BaseModule的源代码目录
    public String GROUTER_SOURCE_PATH = ""
    // 选填，用于外部APP（或浏览器）打开内部页面，如果无需该功能可以不填写
    public String GROUTER_SCHEME = 'grouter'
    // 选填， 和GROUTER_SCHEME参数联合使用，{GROUTER_SCHEME}://{GROUTER_HOST}/user
    public String GROUTER_HOST = 'grouter'

    // --- 多工程模式 ---
    // 选填，多工程模式开关
    public boolean GROUTER_MULTI_PROJECT_MODE = false
    // 选填，多工程模式生成，如果APP是多工程模式，为每一个工程配置独立的名称，同时需要手工在Application配置
    public String GROUTER_MULTI_PROJECT_NAME = "MainProject"
    // 选填，多工程模式生成，如果APP是多工程模式，为每一个工程配置独立的名称，避免冲突
//    public String GROUTER_CENTER_NAME = "GActivityCenter"
    // 选填，多工程模式生成，如果APP是多工程模式，为每一个工程配置独立的名称，避免冲突
//    public String GCOMPONENT_CENTER_NAME = "GComponentCenter"
}