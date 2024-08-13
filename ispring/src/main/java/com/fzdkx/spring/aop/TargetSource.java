package com.fzdkx.spring.aop;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * 目标类
 */
public class TargetSource {

    // 目标类对象
    private final Object target;

    public TargetSource(Object target) {
        this.target = target;
    }

    // 获取目标类接口CLass对象
    public Class<?>[] getTargetInterfaceClass() {
        return this.target.getClass().getInterfaces();
    }

    // 获取目标类对象
    public Object getTarget() {
        return this.target;
    }
}
