package com.grouter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Wiki on 19/7/28.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RouterActivity {

    /**
     * 路径，eg. @RouterActivity("movie/worker")
     * 如果要匹配多个路径请使用{,}分割 eg. @RouterActivity("account/login,account/register")
     */
    String value() default "";

    /**
     * 是否可以从外部APP打开
     * 当path不为空时候，默认是true，否则默认是false，
     * 如果设置成false，那么值就是false，不随path改变。
     */
    boolean exported() default true;

    /**
     * 用于文档，名称
     */
    String name() default "";
    /**
     * 用于文档，描述
     */
    String description() default "";
}
