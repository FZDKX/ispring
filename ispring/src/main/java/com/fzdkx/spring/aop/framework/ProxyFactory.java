package com.fzdkx.spring.aop.framework;

import com.fzdkx.spring.aop.AdvisedSupport;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * 代理工厂：专门创建代理对象
 */
public class ProxyFactory {

    private AdvisedSupport advisedSupport;

    public ProxyFactory(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    // 获取代理
    public Object getObject() {
        return createAopProxy().getProxy();
    }

    // 创建AOP代理
    private AopProxy createAopProxy() {
        Class<?>[] interfaceClass = advisedSupport.getTargetSource().getTargetInterfaceClass();
        // 如果没有接口，采用CGLIB
        if (interfaceClass == null || interfaceClass.length == 0){
            return new CglibAopProxy(advisedSupport);
        }
        // 如果有接口，采用JDK
        else {
            return new JdkDynamicAopProxy(advisedSupport);
        }
    }
}
