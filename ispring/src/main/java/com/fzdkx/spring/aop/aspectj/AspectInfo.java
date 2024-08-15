package com.fzdkx.spring.aop.aspectj;

import com.fzdkx.spring.aop.DefaultAspect;

/**
 * @author 发着呆看星
 * @create 2024/8/15
 * 组合类
 */
public class AspectInfo {
    private int order = 10;
    private DefaultAspect aspectObject;
    private AspectJExpressionPointcut pointcut;

    public AspectInfo(int order, DefaultAspect aspectObject , String expression) {
        this.order = order;
        this.aspectObject = aspectObject;
        this.pointcut = new AspectJExpressionPointcut(expression);
    }

    public int getOrder() {
        return order;
    }

    public DefaultAspect getAspectObject() {
        return aspectObject;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setAspectObject(DefaultAspect aspectObject) {
        this.aspectObject = aspectObject;
    }

    public AspectJExpressionPointcut getPointcut() {
        return pointcut;
    }

    public void setPointcut(AspectJExpressionPointcut pointcut) {
        this.pointcut = pointcut;
    }
}
