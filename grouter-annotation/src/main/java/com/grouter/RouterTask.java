package com.grouter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 默认使用空构造方法实现，如果带有参数的构造方法需要实现就要使用ComponentConstructor注解
 * Created by Wiki on 19/8/14.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RouterTask {
    /**
     * path
     */
    String value() default "";

    /**
     * 用于文档，用于文档标记返回值使用
     */
    String returns() default "";

    /**
     * 用于文档，名称
     */
    String name() default "";

    /**
     * 用于文档，描述
     */
    String description() default "";
}
