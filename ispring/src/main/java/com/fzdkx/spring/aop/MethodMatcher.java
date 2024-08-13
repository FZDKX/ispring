package com.fzdkx.spring.aop;

import java.lang.reflect.Method;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * 方法匹配：找到 表达式表示的范围内 匹配的 目标类和方法
 */
public interface MethodMatcher{

    boolean matches(Method method, Class<?> targetClass);
}
