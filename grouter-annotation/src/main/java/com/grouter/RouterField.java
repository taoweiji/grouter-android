package com.grouter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

/**
 * Created by Wiki on 19/7/28.
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RouterField {
    /**
     * 字段名称，用于解析URL参数使用
     */
    String value() default "";
    /**
     * 用于文档，描述
     */
    String description() default "";
}
