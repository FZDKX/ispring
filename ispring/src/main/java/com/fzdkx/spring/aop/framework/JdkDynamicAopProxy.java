package com.fzdkx.spring.aop.framework;

import com.fzdkx.spring.aop.AdvisedSupport;
import org.aopalliance.intercept.MethodInterceptor;

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
        // 如果当前方法匹配切点表达式，那么需要进行方法增强
        if (advised.getMethodMatcher().matches(method, target.getClass())) {
            // 获取方法拦截器
            MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
            // 方法拦截器 需要 方法调用实例，进行方法调用
            return methodInterceptor.invoke(new ReflectiveMethodInvocation(target, method, args));
        }
        // 普通调用，没有增强
        return method.invoke(target, args);
    }
}
