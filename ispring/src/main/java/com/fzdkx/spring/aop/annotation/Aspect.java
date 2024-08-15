package com.fzdkx.spring.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 发着呆看星
 * @create 2024/8/15
 */
@Target(ElementType.TYPE) // 只能标注在类上
@Retention(RetentionPolicy.RUNTIME) // 可以运行时获取信息
public @interface Aspect {

    // 切点表达式，表示作用于哪些方法上
    String value();
}
