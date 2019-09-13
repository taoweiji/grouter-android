package com.grouter.compiler;

import java.util.ArrayList;
import java.util.List;

public class ComponentModel implements Comparable<ComponentModel> {
    public String protocol;
    public String implement;
    public List<ConstructorBean> constructors = new ArrayList<>();
    public String path;
    public String module;
    public String name;
    public String description;

    @Override
    public int compareTo(ComponentModel model) {
        if (this.module == null) {
            this.module = "";
        }
        if (model.module == null) {
            model.module = "";
        }
        if (!module.equals(model.module)) {
            return this.module.compareTo(model.module);
        }
        return this.implement.compareTo(model.implement);
    }
    public static class ConstructorBean {
        public List<String> parameterTypes = new ArrayList<>();
        public List<String> parameterNames = new ArrayList<>();
    }
}