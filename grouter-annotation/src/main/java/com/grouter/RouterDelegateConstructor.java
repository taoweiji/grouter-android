package com.grouter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface RouterDelegateConstructor {
    /**
     * 用于文档，名称
     */
    String name() default "";
    /**
     * 用于文档，描述
     */
    String description() default "";
}