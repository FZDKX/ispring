package com.fzdkx.spring.test;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 */
@Slf4j
public class UserServiceInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        log.debug("对方法进行--前置--增强");
        Object result = methodInvocation.proceed();
        log.debug("对方法进行--后置--增强");
        return result;
    }

}
