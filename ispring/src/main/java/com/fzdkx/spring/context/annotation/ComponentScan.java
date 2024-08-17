package com.fzdkx.spring.context.annotation;

import java.lang.annotation.*;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ComponentScan {
    // 需要扫描的包 basePackages
    String[] value() default {};
}
