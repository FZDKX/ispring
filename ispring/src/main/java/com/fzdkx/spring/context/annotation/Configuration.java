package com.fzdkx.spring.context.annotation;

import java.lang.annotation.*;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 * 标识为配置文件
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {

    // 指定bean的名称
    String value() default "";

}
