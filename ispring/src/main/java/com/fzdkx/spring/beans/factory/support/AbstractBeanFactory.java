package com.fzdkx.spring.beans.factory.support;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.exception.NoSuchBeanDefinitionException;
import com.fzdkx.spring.beans.factory.BeanFactory;
import com.fzdkx.spring.beans.factory.FactoryBean;
import com.fzdkx.spring.beans.factory.config.BeanDefinition;
import com.fzdkx.spring.beans.factory.config.BeanPostProcessor;
import com.fzdkx.spring.beans.factory.config.ConfigurableBeanFactory;
import com.fzdkx.spring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.fzdkx.spring.util.BeanFactoryUtil;
import com.fzdkx.spring.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 * 抽象Bean工厂
 * 模板方法设计模式：定义容器刷新12步
 * |-- 继承DefaultSingletonBeanRegistry，可以注册Bean
 * |-- 实现ConfigurableBeanFactory，对BeanPostProcess进行管理
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private volatile boolean hasInstantiationAwareBeanPostProcessors;

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    @Override
    public boolean containsBean(String name) throws BeansException {
        return containsSingleton(name) || getBeanDefinition(name) != null;
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return doCetBean(name, null);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return doCetBean(name, requiredType);
    }

    private <T> T doCetBean(String name, Class<T> requiredType) {
        // 转换名称：如果是&开头，去掉&
        String beanName = transformedBeanName(name);
        // 想尝试从缓存中获取
        Object sharedInstance = getSingleton(beanName);
        Object bean;
        // 获取到了：可能是FactoryBean
        if (sharedInstance != null) {
            // 如果是工厂Bean，那么调用getObject创建Bean
            bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
        }
        // 如果未获取到，进行创建
        else {
            BeanDefinition beanDefinition = getBeanDefinition(beanName);
            if (beanDefinition == null) {
                throw new NoSuchBeanDefinitionException(beanName);
            }
            // 是单例
            if (beanDefinition.isSingleton()) {
                // 创建一个Bean，完成依赖注入与初始化，并将这个Bean添加到缓存中
                sharedInstance = getSingleton(beanName, () -> createBean(beanName, beanDefinition));
                // 如果这个bean是一个FactoryBean
                bean = getObjectForBeanInstance(sharedInstance, name, beanName, beanDefinition);
            }
            // 不是单例
            else if (beanDefinition.isPrototype()) {
                // 创建一个Bean，完成依赖注入与初始化
                bean = createBean(beanName, beanDefinition);
            } else {
                throw new BeansException("bean 类型错误");
            }
        }
        return (T) bean;
    }

    private String transformedBeanName(String name) {
        // 转换名称
        // 如果不以 & 开头，直接返回
        if (!name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX)) {
            return name;
        }
        // 如果以 & 开头，将前缀&都去掉
        String beanName = name;
        do {
            beanName = beanName.substring(BeanFactory.FACTORY_BEAN_PREFIX.length());
        } while (beanName.startsWith(BeanFactory.FACTORY_BEAN_PREFIX));
        return beanName;
    }

    /**
     * @param beanInstance 容器中获取的对象
     * @param name         原本的 name
     * @param beanName     去掉&之后的 name
     * @param bd           Bean定义信息
     *                     <p>
     *                     当容器初始化时：FactoryBean 创建时会加上 & , 这时会对FactoryBean 打上标记
     *                     当我们需要FactoryBean创建的bean时：原名称没有 & ，我们从容器中获取bean
     *                     |-- 如果bean 不是FactoryBean：直接返回
     *                     |-- 如果是 FactoryBean
     *                     |-- 如果有 BeanDefinition，表示这个bean不是从缓存中获取的，再次打上标签，返回
     *                     |-- 如果没有 BeanDefinition，表示这个bean是从缓存中获取的
     *                     |-- 尝试从 FactoryBean创建的Bean的缓存中 获取Bean
     *                     |-- 如果获取不到，带哦有FactoryBean的getObject()方法获取
     */
    private Object getObjectForBeanInstance(Object beanInstance, String name, String beanName, BeanDefinition bd) {
        // 如果以&开头：如果是FactoryBean，设置isFactoryBean == true，返回 FactoryBean实例
        if (BeanFactoryUtil.isFactoryDereference(name)) {
            // 如果名称是 & 但是不是 FactoryBean，报错
            if (!(beanInstance instanceof FactoryBean)) {
                throw new BeansException(beanName);
            }
            // 是工厂bean
            if (bd != null) {
                bd.setIsFactoryBean(true);
            }
            // 返回工厂Bean
            return beanInstance;
        }
        // 不以&开头
        // 如果不是FactoryBean，直接返回
        if (!(beanInstance instanceof FactoryBean)) {
            return beanInstance;
        }
        // 如果是 FactoryBean
        Object object = null;
        if (bd != null) {
            bd.setIsFactoryBean(true);
        }
        // 如果 BeanDefinition == null , 代表要获取FactoryBean制造的Bean
        else {
            // 尝试从缓存中获取 创建的bean
            object = getCachedObjectForFactoryBean(beanName);
        }
        // 如果缓存中没有，那么创建
        if (object == null) {
            FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
            object = getObjectFromFactoryBean(factory, beanName);
        }
        return object;
    }


    @Override
    public boolean isSingleton(String name) throws BeansException {
        BeanDefinition beanDefinition = getBeanDefinition(name);
        if (beanDefinition == null) {
            throw new NoSuchBeanDefinitionException(name);
        }
        return beanDefinition.isSingleton();
    }

    @Override
    public boolean isPrototype(String name) throws BeansException {
        BeanDefinition beanDefinition = getBeanDefinition(name);
        if (beanDefinition == null) {
            throw new NoSuchBeanDefinitionException(name);
        }
        return beanDefinition.isPrototype();
    }

    @Override
    public Class<?> getType(String name) throws BeansException {
        BeanDefinition beanDefinition = getBeanDefinition(name);
        if (beanDefinition == null) {
            throw new NoSuchBeanDefinitionException(name);
        }
        return beanDefinition.getBeanClass();
    }

    // 创建Bean
    protected abstract Object createBean(String name, BeanDefinition beanDefinition) throws BeansException;

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;


    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        if (beanPostProcessor != null) {
            this.beanPostProcessors.remove(beanPostProcessor);
            this.beanPostProcessors.add(beanPostProcessor);
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                this.hasInstantiationAwareBeanPostProcessors = true;
            }
        }
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }

    protected abstract boolean containsBeanDefinition(String beanName);

    public boolean isHasInstantiationAwareBeanPostProcessors() {
        return this.hasInstantiationAwareBeanPostProcessors;
    }

}
