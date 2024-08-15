package com.fzdkx.spring.beans.factory.support;

import com.fzdkx.spring.beans.exception.BeanCurrentlyInCreationException;
import com.fzdkx.spring.beans.exception.BeanInstanceException;
import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.DisposableBean;
import com.fzdkx.spring.beans.factory.ObjectFactory;
import com.fzdkx.spring.beans.factory.config.SingletonBeanRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 * 单例Bean注册中心
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    // 一级缓存：保存完整的单例Bean
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    // 二级缓存：简单的单例对象，如果要代理，那么是代理对象
    private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);

    // 三级缓存：保存ObjectFactory，getObject方法，返回一个原始Bean 或者 代理对象
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

    // 当前正在创建的单例Bean
    private final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    // 记录有销毁逻辑的Bean
    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    // 获取一个Bean，参数从缓存中获取
    @Override
    public Object getSingleton(String beanName) {
        // 先从一级缓存中获取
        Object singletonObject = this.singletonObjects.get(beanName);
        if (singletonObject == null) {
            // 从二级缓存中获取
            singletonObject = this.earlySingletonObjects.get(beanName);
            if (singletonObject == null) {
                // 加锁，创建Bean
                synchronized (this.singletonObjects) {
                    // 检查：从一级缓存中获取
                    singletonObject = this.singletonObjects.get(beanName);
                    if (singletonObject == null) {
                        // 检查：从二级缓存中获取
                        singletonObject = this.earlySingletonObjects.get(beanName);
                        if (singletonObject == null) {
                            // 三级缓存中是否有该Bean
                            ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                            // 如果有，调用getObject
                            if (singletonFactory != null) {
                                singletonObject = singletonFactory.getObject();
                                this.earlySingletonObjects.put(beanName, singletonObject);
                                this.singletonFactories.remove(beanName);
                            }
                        }
                    }
                }
            }
        }
        return singletonObject;
    }

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        this.singletonObjects.put(beanName, singletonObject);
    }

    // 获取一个bean，带有 ObjectFactory 的获取
    public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
        // 从一级缓存中获取
        synchronized (this.singletonObjects) {
            Object bean = singletonObjects.get(beanName);
            if (bean == null) {
                // 记录该Bean正在加载
                beforeSingletonCreation(beanName);
                // 创建Bean
                bean = singletonFactory.getObject();
                // 移除正在加载的状态
                afterSingletonCreation(beanName);
                // 添加
                addSingleton(beanName);
            }
            return bean;
        }

    }

    // 向一级缓存容器中添加Bean
    public void addSingleton(String beanName) {
        synchronized (this.singletonObjects) {
            // 先从三级缓存获取
            ObjectFactory<?> objectFactory = singletonFactories.get(beanName);
            Object singletonBean;
            // 三级缓存没有
            if (objectFactory == null) {
                // 从二级缓存中获取
                singletonBean = this.earlySingletonObjects.get(beanName);
            }
            // 三级缓存有
            else {
                singletonBean = objectFactory.getObject();
            }
            // 如果为null
            if (singletonBean == null) {
                throw new BeansException("bean 创建错误");
            }
            singletonObjects.put(beanName, singletonBean);
            earlySingletonObjects.remove(beanName);
            singletonFactories.remove(beanName);
        }
    }

    // 向三级缓存中添加Bean
    public void addSingletonFactory(String name, ObjectFactory<?> singletonFactory) {
        if (singletonFactory == null) {
            throw new BeanInstanceException("添加至三级缓存失败");
        }
        synchronized (this.singletonObjects) {
            if (!this.singletonObjects.containsKey(name)) {
                // 加入三级缓存
                this.singletonFactories.put(name, singletonFactory);
                // 从二级缓存中移除，如果有的化
                this.earlySingletonObjects.remove(name);
            }
        }
    }

    // 在创建单例Bean之前进行标记
    protected void beforeSingletonCreation(String beanName) {
        // 标记正在创建成功，直接返回
        // 标记失败，移除
        if (!this.singletonsCurrentlyInCreation.add(beanName)) {
            throw new BeanCurrentlyInCreationException("发生循环依赖");
        }
    }

    // 移除正在加载的状态
    private void afterSingletonCreation(String beanName) {
        if (!this.singletonsCurrentlyInCreation.remove(beanName)) {
            throw new IllegalStateException("当前bean正在创建");
        }
    }

    public boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }

    public boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeans.put(beanName, bean);
    }

    // 销毁单例bean
    public void destroySingletons() {
        Object[] beanNames = this.disposableBeans.keySet().toArray();
        for (int i = beanNames.length - 1; i >= 0; i--) {
            String name = (String) beanNames[i];
            // 移除单例
            removeSingleton(name);
            // 移除销毁
            DisposableBean disposableBean = disposableBeans.remove(name);
            // 调用销毁方法
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new RuntimeException("销毁失败");
            }
        }
    }

    // 移除单例
    protected void removeSingleton(String beanName) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.remove(beanName);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
        }
    }
}
