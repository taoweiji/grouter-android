package com.grouter.compiler;

import java.util.ArrayList;
import java.util.List;

public class TaskModel implements Comparable<TaskModel> {
    public String module;
    public String name;
    public String path;
    public String type;
    public String argument;
    public String description;
    public String returns;
    public List<ParameterModel> params = new ArrayList<>();

    @Override
    public int compareTo(TaskModel model) {
        if (this.module == null) {
            this.module = "";
        }
        if (model.module == null) {
            model.module = "";
        }
        if (this.path == null) {
            this.path = "";
        }
        if (model.path == null) {
            model.path = "";
        }
        if (!module.equals(model.module)) {
            return this.module.compareTo(model.module);
        }
        if (!path.equals(model.path)) {
            return this.path.compareTo(model.path);
        }
        return this.type.compareTo(model.type);
    }
}