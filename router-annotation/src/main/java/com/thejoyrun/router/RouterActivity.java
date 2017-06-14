package com.thejoyrun.router;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Wiki on 16/7/28.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RouterActivity {
    String[] value();
//    String[] longExtra() default "";

//    String[] intExtra() default "";

//    String[] StringExtra() default "";
}
