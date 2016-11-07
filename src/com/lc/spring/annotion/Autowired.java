package com.lc.spring.annotion;

import java.lang.annotation.*;

/**
 * Created by liuchengli on 2016/10/30.
 */
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
