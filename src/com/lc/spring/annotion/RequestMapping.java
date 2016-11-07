package com.lc.spring.annotion;

import java.lang.annotation.*;

/**
 * Created by liuchengli on 2016/10/29.
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
