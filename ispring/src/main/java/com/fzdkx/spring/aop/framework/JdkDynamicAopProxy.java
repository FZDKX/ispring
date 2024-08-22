package com.fzdkx.spring.aop.framework;

import com.fzdkx.spring.aop.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * JDK动态代理AOP
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    // AOP包装类
    private final AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        // 创建代理类，并返回
        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                advised.getTargetSource().getTargetInterfaceClass(),
                this
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object target = advised.getTargetSource().getTarget();
        return advised.getMethodInterceptor().invoke(new ReflectiveMethodInvocation(target, method, args));
    }
}
