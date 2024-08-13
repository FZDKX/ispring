package com.fzdkx.spring.aop;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 */
public interface Pointcut {
    // 返回此切入点的 ClassFilter
    ClassFilter getClassFilter();

    // 返回此切入点的 MethodMatcher
    MethodMatcher getMethodMatcher();
}
