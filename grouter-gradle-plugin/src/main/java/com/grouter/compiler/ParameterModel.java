package com.grouter.compiler;

public class ParameterModel implements Comparable<ParameterModel> {
    /**
     * 字段名称
     */
    public String name;
    /**
     * 支持类型，支持基本类型、基本类型数组、List、Map、Activity、Context、Application
     */
    public String type;

    /**
     * 原始类型
     */
    public String rawType;
    /**
     * URL解析名称
     */
    public String queryName;
    /**
     * 描述
     */
    public String description;

    @Override
    public int compareTo(ParameterModel model) {
        return this.type.compareTo(model.type);
    }
}