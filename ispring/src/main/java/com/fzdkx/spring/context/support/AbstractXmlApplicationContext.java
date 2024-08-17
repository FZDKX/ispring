package com.fzdkx.spring.context.support;

import com.fzdkx.spring.beans.factory.support.DefaultListableBeanFactory;
import com.fzdkx.spring.beans.factory.xml.XmlBeanDefinitionReader;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 * 基于XML的应用上下文
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext{
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        // 读取XML配置文件
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory, this);
        String[] configLocations = getConfigLocations();
        Set<String> result = new HashSet<>();
        if (configLocations != null){
            result = reader.loadBeanDefinitions(configLocations);
        }
        this.getBeanFactory().getLocations().addAll(result);
    }

    // 将获取资源的方式下放，交给子类实现
    protected abstract String[] getConfigLocations();
}
