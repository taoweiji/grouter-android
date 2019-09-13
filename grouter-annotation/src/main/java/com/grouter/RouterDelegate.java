package com.grouter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 非下沉式代理组件，RouterDelegate需要搭配RouterDelegateMethod使用
 * Created by Wiki on 19/7/28.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RouterDelegate {
    /**
     * 用于文档，名称
     */
    String name() default "";

    /**
     * 用于文档，描述
     */
    String description() default "";
}