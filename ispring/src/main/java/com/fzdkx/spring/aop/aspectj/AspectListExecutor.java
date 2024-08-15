package com.fzdkx.spring.aop.aspectj;

import com.fzdkx.spring.aop.MethodMatcher;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

/**
 * @author 发着呆看星
 * @create 2024/8/15
 */
public class AspectListExecutor implements MethodInterceptor {

    // Aspect集合，集合中的Aspect都需要对当前方法进行拦截
    private final List<AspectInfo> sortedAspectInfos;

    public AspectListExecutor(List<AspectInfo> aspectInfos) {
        // 排序后
        this.sortedAspectInfos = sortAspectInfo(aspectInfos);
    }

    private List<AspectInfo> sortAspectInfo(List<AspectInfo> aspectInfos) {
        // 根据order升序排列
        aspectInfos.sort(Comparator.comparingInt(AspectInfo::getOrder));
        return aspectInfos;
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Object returnValue = null;
        if (sortedAspectInfos == null || sortedAspectInfos.isEmpty()) {
            return null;
        }
        try {
            // 1：按照order顺序升序执行 before方法
            invokeBeforeAdvices(mi);
            // 2: 执行被代理类的方法
            returnValue = invokeTarget(mi);
            // 3：如果执行成功，执行 afterReturning方法
            returnValue = invokeAfterReturning(mi, returnValue);
        } catch (Exception e) {
            // 如果报错：执行异常方法
            invokeThrowing(mi, e);
        } finally {
            // 执行after方法
            invokeAfter(mi);
        }
        return returnValue;
    }

    private void invokeBeforeAdvices(MethodInvocation mi) throws Throwable {
        Method method = mi.getMethod();
        Object[] args = mi.getArguments();
        Object target = mi.getThis();
        for (AspectInfo aspectInfo : sortedAspectInfos) {
            MethodMatcher methodMatcher = aspectInfo.getPointcut().getMethodMatcher();
            if (methodMatcher.matches(method)) {
                // 方法增强调用
                aspectInfo.getAspectObject().before(target, method, args);
            }
        }
    }

    private Object invokeTarget(MethodInvocation mi) throws Throwable {
        return mi.proceed();
    }

    private Object invokeAfterReturning(MethodInvocation mi, Object returnValue) throws Throwable {
        Object result = null;
        Method method = mi.getMethod();
        Object[] args = mi.getArguments();
        Object target = mi.getThis();
        for (AspectInfo aspectInfo : sortedAspectInfos) {
            MethodMatcher methodMatcher = aspectInfo.getPointcut().getMethodMatcher();
            if (methodMatcher.matches(method)) {
                // 方法增强调用
                result = aspectInfo.getAspectObject().afterReturning(target, method, args, returnValue);
            }
        }
        return result;
    }

    private void invokeThrowing(MethodInvocation mi, Exception e) throws Throwable {
        Method method = mi.getMethod();
        Object[] args = mi.getArguments();
        Object target = mi.getThis();
        for (int i = sortedAspectInfos.size() - 1; i >= 0; i--) {
            AspectInfo aspectInfo = sortedAspectInfos.get(i);
            MethodMatcher methodMatcher = aspectInfo.getPointcut().getMethodMatcher();
            if (methodMatcher.matches(method)) {
                // 方法增强调用
                aspectInfo.getAspectObject().afterThrowing(target, method, args, e);
            }
        }
    }

    private void invokeAfter(MethodInvocation mi) throws Throwable {
        Method method = mi.getMethod();
        Object[] args = mi.getArguments();
        Object target = mi.getThis();
        for (int i = sortedAspectInfos.size() - 1; i >= 0; i--) {
            AspectInfo aspectInfo = sortedAspectInfos.get(i);
            MethodMatcher methodMatcher = aspectInfo.getPointcut().getMethodMatcher();
            if (methodMatcher.matches(method)) {
                // 方法增强调用
                aspectInfo.getAspectObject().after(target, method, args);
            }
        }
    }
}
