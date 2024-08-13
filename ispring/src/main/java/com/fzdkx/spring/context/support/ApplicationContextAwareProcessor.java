package com.fzdkx.spring.context.support;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.config.BeanPostProcessor;
import com.fzdkx.spring.context.ApplicationContext;
import com.fzdkx.spring.context.ApplicationContextAware;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 * 上下文增强器：感知 上下文Aware接口，如果bean需要一个上下文对象，那么就将上下文传递给它
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {
    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 如果bean实现ApplicationContextAware 接口，那么就调用bean的 setApplicationContext方法
        // 为bean设置 容器属性
        if (bean instanceof ApplicationContextAware){
            ((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
