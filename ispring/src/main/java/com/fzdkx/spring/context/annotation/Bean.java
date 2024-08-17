package com.fzdkx.spring.context.annotation;

import java.lang.annotation.*;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 */
@Target(ElementType.METHOD)  // 类和方法
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

    // bean name
    String value() default "";

    String initMethod() default "";

    String destroyMethod() default "";

}
