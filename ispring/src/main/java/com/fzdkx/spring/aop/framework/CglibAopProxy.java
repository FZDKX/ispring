package com.fzdkx.spring.aop.framework;

import com.fzdkx.spring.aop.AdvisedSupport;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 */
public class CglibAopProxy implements AopProxy {
    private final AdvisedSupport advised;

    public CglibAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    // 创建代理对象
    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        // 设置父类，父类就是目标对象
        enhancer.setSuperclass(advised.getTargetSource().getTarget().getClass());
        // 设置接口，接口就是目标对象实现的接口
        enhancer.setInterfaces(advised.getTargetSource().getTargetInterfaceClass());
        // 设置回调函数
        enhancer.setCallback(new DynamicAdvisedInterceptor(advised));
        return enhancer.create();
    }

    // 静态内部类 ，实现MethodInterceptor，进行方法调用
    public static class DynamicAdvisedInterceptor implements MethodInterceptor {

        private final AdvisedSupport advised;

        public DynamicAdvisedInterceptor(AdvisedSupport advised) {
            this.advised = advised;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object target = advised.getTargetSource().getTarget();
            return advised.getMethodInterceptor().invoke(new ReflectiveMethodInvocation(target, method, args));
        }
    }

}
