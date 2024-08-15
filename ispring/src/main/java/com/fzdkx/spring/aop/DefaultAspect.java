package com.fzdkx.spring.aop;

import java.lang.reflect.Method;

/**
 * @author 发着呆看星
 * @create 2024/8/15
 * 默认切面
 */
public abstract class DefaultAspect {

    /**
     * 钩子方法：目标方法执行前执行
     * @param target 目标类
     * @param method      目标类执行的方法
     * @param args        方法参数
     */
    public void before(Object target, Method method, Object[] args) {

    }

    /**
     * 钩子方法：目标方法执行成功之后执行
     * @param target 目标类
     * @param method      目标类执行的方法
     * @param args        方法参数
     * @param returnValue 目标父方法返回值
     */
    public Object afterReturning(Object target, Method method, Object[] args, Object returnValue) throws Throwable {
        return returnValue;
    }

    /**
     * 钩子方法：目标方法执行失败抛出异常之后执行
     * @param target 目标类
     * @param method 目标方法
     * @param args 目标方法参数
     * @param e 目标方法执行过程中出现的异常
     */
    public void afterThrowing(Object target, Method method, Object[] args, Throwable e) throws Throwable {

    }

    /**
     * 钩子方法：在目标方法执行之后，无论如何都会执行的方法
     * @param target 目标类
     * @param method 目标方法
     * @param args 目标方法参数
     */
    public void after(Object target, Method method, Object[] args) throws Throwable {

    }

}
