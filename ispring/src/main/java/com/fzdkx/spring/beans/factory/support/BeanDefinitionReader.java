package com.fzdkx.spring.beans.factory.support;

import com.fzdkx.spring.beans.exception.BeanDefinitionStoreException;
import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.core.io.Resource;
import com.fzdkx.spring.core.io.ResourceLoader;
import java.util.Set;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 * Bean定义信息读取接口
 */
public interface BeanDefinitionReader {

    // 获取BeanDefinition注册器
    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    // 加载BeanDefinition信息，返回找到的BeanDefinition的数量
    Set<String> loadBeanDefinitions(String location) throws BeanDefinitionStoreException;

    Set<String> loadBeanDefinitions(Resource resource) throws BeansException;

    Set<String> loadBeanDefinitions(Resource... resources) throws BeansException;
    Set<String> loadBeanDefinitions(String... locations) throws BeansException;

}
