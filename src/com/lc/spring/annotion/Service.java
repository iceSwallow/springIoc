package com.lc.spring.annotion;

import java.lang.annotation.*;

/**
 * Created by liuchengli on 2016/10/29.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
