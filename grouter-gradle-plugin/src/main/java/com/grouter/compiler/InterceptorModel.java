package com.grouter.compiler;


public class InterceptorModel  implements Comparable<InterceptorModel> {
    public String type;

    public int priority;
    public String module;

    @Override
    public int compareTo(InterceptorModel interceptorModel) {
        return this.type.compareTo(interceptorModel.type);
    }
}
