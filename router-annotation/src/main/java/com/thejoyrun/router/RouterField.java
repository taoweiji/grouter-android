package com.thejoyrun.router;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Wiki on 16/7/28.
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface RouterField {
    String[] value();
}
