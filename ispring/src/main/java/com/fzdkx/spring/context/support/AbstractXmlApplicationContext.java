package com.fzdkx.spring.context.support;

import com.fzdkx.spring.beans.factory.support.DefaultListableBeanFactory;
import com.fzdkx.spring.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 * 基于XML的应用上下文
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext{
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory, this);
        String[] configLocations = getConfigLocations();
        if (configLocations != null){
            reader.loadBeanDefinitions(configLocations);
        }
    }

    // 将获取资源的方式下放，交给子类实现
    protected abstract String[] getConfigLocations();
}
