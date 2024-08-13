package com.fzdkx.spring.beans.factory.support;

import com.fzdkx.spring.core.io.DefaultResourceLoader;
import com.fzdkx.spring.core.io.ResourceLoader;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    // 持有BeanDefinition注册器
    private final BeanDefinitionRegistry registry;

    // 持有资源加载器
    private ResourceLoader resourceLoader;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this(registry, new DefaultResourceLoader());
    }

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }
}
