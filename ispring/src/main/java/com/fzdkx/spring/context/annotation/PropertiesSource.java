package com.fzdkx.spring.context.annotation;

import java.lang.annotation.*;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertiesSource {
    // 加载指定配置文件
    String[] value() default "";
}
