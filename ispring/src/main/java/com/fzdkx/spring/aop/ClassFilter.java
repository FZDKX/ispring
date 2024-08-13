package com.fzdkx.spring.aop;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * 类过滤接口：目的是给切点找到对应的接口和目标类
 */
public interface ClassFilter {
    boolean matches(Class<?> clazz);
}
