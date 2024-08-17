package com.fzdkx.spring.context.support;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 */
public interface AnnotationConfigRegistry {

    // 注册配置类
    void register(Class<?>... componentClasses);

    // 包扫描
    void scan(String... basePackages);
}
