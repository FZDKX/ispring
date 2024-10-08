package com.fzdkx.mytest.aop;

import com.fzdkx.spring.aop.DefaultAspect;
import com.fzdkx.spring.aop.annotation.Aspect;
import com.fzdkx.spring.context.annotation.Component;
import com.fzdkx.spring.context.annotation.Order;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author 发着呆看星
 * @create 2024/8/15
 */
@Slf4j()
@Order(0)
@Aspect("execution(* com.fzdkx.mytest.service.UserService.*(..))")
@Component
public class MyAopTest extends DefaultAspect {

    @Override
    public void before(Object target, Method method, Object[] args) {
        log.debug("前置通知，执行方法：{}，方法参数：{}", method.getName(), args);
    }

    @Override
    public Object afterReturning(Object target, Method method, Object[] args, Object returnValue) throws Throwable {
        log.debug("返回通知，返回值：{}", returnValue);
        return returnValue;
    }

    @Override
    public void afterThrowing(Object target, Method method, Object[] args, Throwable e) throws Throwable {
        log.debug("异常通知：异常消息：{}", e.getMessage());
    }

    @Override
    public void after(Object target, Method method, Object[] args) throws Throwable {
        log.debug("最终通知：~~~~~~~~~~~~");
    }
}
