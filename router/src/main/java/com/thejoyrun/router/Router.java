package com.thejoyrun.router;

/**
 * Created by Wiki on 16/7/28.
 */
public @interface Router {
    String[] longExtra() default "";

    String[] intExtra() default "";

    String[] StringExtra() default "";
}
