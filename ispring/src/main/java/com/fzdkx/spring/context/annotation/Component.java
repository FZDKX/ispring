package com.fzdkx.spring.context.annotation;

import java.lang.annotation.*;

/**
 * @author 发着呆看星
 * @create 2024/8/15
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

    // bean name
    String value() default "";

}
