package com.fzdkx.spring.context.support;

import com.fzdkx.spring.beans.factory.ConfigurableListableBeanFactory;
import com.fzdkx.spring.beans.factory.config.BeanFactoryPostProcessor;
import com.fzdkx.spring.beans.factory.config.BeanPostProcessor;
import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.core.io.DefaultResourceLoader;
import com.fzdkx.spring.context.ConfigurableApplicationContext;

import java.util.Map;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 * 抽象的应用上下文，实现了ConfigurableApplicationContext，定义容器刷新步骤，模板方法
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {
    @Override
    public void refresh() throws BeansException {
        // 1. 创建BeanFactory，加载BeanDefinition
        refreshBeanFactory();
        // 2. 获取BeanFactory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        // 3. 添加 ApplicationContextAwareProcessor，感知bean是否实现ApplicationContextAware接口
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        // 4. 在 Bean 实例化之前，执行BeanFactoryPostProcessors的方法
        invokeBeanFactoryPostProcessors(beanFactory);
        // 5. 在 Bean实例化之前，注册BeanPostProcessors
        registerBeanPostProcessors(beanFactory);
        // 6. 创建所有剩下的单例Bean
        finishBeanFactoryInitialization(beanFactory);
    }

    // 创建所有剩下的单例Bean
    private void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.preInstantiateSingletons();
    }

    // 创建BeanFactory，加载BeanDefinition
    // 进行下放，可能子类会创建处不同的 BeanFactory
    protected abstract void refreshBeanFactory() throws BeansException;

    // 获取BeanFactory，下放，或获取子类真正创建的BenaFactory
    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    // 执行所有BeanFactoryPostProcess的方法
    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        // 获取所有的 BeanFactoryPostProcess
        Map<String, BeanFactoryPostProcessor> map = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        // 遍历这些 BeanFactoryPostProcess，执行方法
        for (BeanFactoryPostProcessor beanFactoryPostProcess : map.values()) {
            // 传入BeanFactory，可以修改BeanDefinition
            beanFactoryPostProcess.postProcessBeanFactory(beanFactory);
        }
    }

    // 注册 BeanPostProcessors
    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> map = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : map.values()) {
            // 添加BeanPostProcessors
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        getBeanFactory().destroySingletons();
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) throws BeansException {
        return getBeanFactory().getBean(name, clazz);
    }

    @Override
    public boolean isSingleton(String name) throws BeansException {
        return getBeanFactory().getBeanDefinition(name).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) throws BeansException {
        return getBeanFactory().getBeanDefinition(name).isPrototype();
    }

    @Override
    public boolean containsBean(String name) throws BeansException {
        return getBeanFactory().containsBean(name);
    }

    @Override
    public Class<?> getType(String name) throws BeansException {
        return getBeanFactory().getBeanDefinition(name).getBeanClass();
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

}