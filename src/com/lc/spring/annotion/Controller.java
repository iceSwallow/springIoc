package com.lc.spring.annotion;

import java.lang.annotation.*;

/**
 * Created by liuchengli on 2016/10/29.
 */
@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Controller {
    String value() default "";
}
