package com.fzdkx.spring.context.support;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 * 可刷新的上下文，包含获取BeanDefinition的方法
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {
    private DefaultListableBeanFactory beanFactory;

    @Override
    protected void refreshBeanFactory() throws BeansException {
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    // 将加载BeanDefinitions的功能下放，交给子类实现，可能是基于XML获取，也可能是基于注解获取
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

    private DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    @Override
    protected DefaultListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
