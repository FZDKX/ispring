package com.fzdkx.spring.context.support;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.ConfigurableListableBeanFactory;
import com.fzdkx.spring.beans.factory.config.BeanFactoryPostProcessor;
import com.fzdkx.spring.beans.factory.config.BeanPostProcessor;
import com.fzdkx.spring.context.ApplicationEvent;
import com.fzdkx.spring.context.ApplicationListener;
import com.fzdkx.spring.context.ConfigurableApplicationContext;
import com.fzdkx.spring.context.event.ApplicationEventMulticaster;
import com.fzdkx.spring.context.event.ContextClosedEvent;
import com.fzdkx.spring.context.event.ContextRefreshedEvent;
import com.fzdkx.spring.context.event.SimpleApplicationEventMulticaster;
import com.fzdkx.spring.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 * 抽象的应用上下文，实现了ConfigurableApplicationContext，定义容器刷新步骤，模板方法
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {
    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    // 持有事件广播器
    private ApplicationEventMulticaster applicationEventMulticaster;

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
        // 6. 初始化事件广播器，并向容器中注册
        initApplicationEventMulticaster();
        // 7. 创建并注册事件监听器
        registerListeners();
        // 8. 创建所有剩下的单例Bean
        finishBeanFactoryInitialization(beanFactory);
        // 9. 发布容器刷新完成事件
        finishRefresh();
    }


    private void finishRefresh() {
        // 创建容器刷新事件，传入触发事件的源-容器
        publishEvent(new ContextRefreshedEvent(this));
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        // 广播事件
        applicationEventMulticaster.multicastEvent(event);
    }

    private void registerListeners() {
        // 获取所有的事件监听器
        Collection<ApplicationListener> listeners = getBeansOfType(ApplicationListener.class).values();
        // 添加进入set
        for (ApplicationListener listener : listeners) {
            applicationEventMulticaster.addApplicationListener(listener);
        }
    }

    private void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster();
        // 向一级缓存中，注册实例
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
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
        // 发布容器关闭事件
        publishEvent(new ContextClosedEvent(this));
        // 执行销毁单例Bean的方法
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
        // 获取type类型的bean，如果没有，获取BeanDefinition创建，会走整个Bean生命周期流程
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

}