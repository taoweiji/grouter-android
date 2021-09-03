package com.grouter.compiler;


import java.util.ArrayList;
import java.util.List;

public class RouterDelegateModel  implements Comparable<RouterDelegateModel> {
    /**
     * type : com.grouter.demo.service.UserListTask
     * methods : [{"name":"getUser","returns":"User","parameterTypes":["String","int"],"parameterNames":["uid","limt"]}]
     * constructors : [{"parameterTypes":["String","int"],"parameterNames":["uid","limt"]}]
     */

    public String type;
    public List<MethodsBean> methods = new ArrayList<>();
    public List<ConstructorBean> constructor = new ArrayList<>();
    public String module;

    @Override
    public int compareTo(RouterDelegateModel model) {
        if (this.module == null) {
            this.module = "";
        }
        if (model.module == null) {
            model.module = "";
        }
        if (!module.equals(model.module)) {
            return this.module.compareTo(model.module);
        }
        return this.type.compareTo(model.type);
    }

    public static class MethodsBean {
        /**
         * name : getUser
         * returns : User
         * parameterTypes : ["String","int"]
         * parameterNames : ["uid","limt"]
         */
        public String name;
        public String returns;
        public List<String> parameterTypes = new ArrayList<>();
        public List<String> parameterNames = new ArrayList<>();
    }

    public static class ConstructorBean {
        public List<String> parameterTypes = new ArrayList<>();
        public List<String> parameterNames = new ArrayList<>();
    }
}
