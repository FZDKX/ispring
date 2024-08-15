package com.fzdkx.spring.context.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 发着呆看星
 * @create 2024/8/15
 */
@Target({ElementType.TYPE})  // 只能作用在类上
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
    // 优先级，越小越优先，决定Aspect的优先级
    int value();
}
