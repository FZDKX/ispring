package com.fzdkx.spring.context.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 发着呆看星
 * @create 2024/8/15
 */
@Target({ElementType.TYPE, ElementType.METHOD})  // 类和方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface Lazy {
    boolean value() default true;
}
