package com.fzdkx.spring.beans.factory.support;

import com.fzdkx.spring.beans.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 * FactoryBean注册服务
 */
public class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

    // 默认空对象
//    private final Object NULL_OBJECT = new Object();

    // 存储FactoryBean制造的Bean
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    // 注册FactoryBean


    // 获取FactoryBean的已制造的Bean
    protected Object getCachedObjectForFactoryBean(String beanName) {
        return this.factoryBeanObjectCache.get(beanName);
    }

    // 从FactoryBean中获取对象
    protected Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName) {
        // 是单例Bean
        if (factory.isSingleton()) {
            // 获取缓存对象
            Object object = this.factoryBeanObjectCache.get(beanName);
            if (object == null) {
                // 调用FactoryBean的getObject方法，获取对象
                object = doGetObjectFromFactoryBean(factory, beanName);
                // 存入缓存
                this.factoryBeanObjectCache.put(beanName, object);
            }
            return object;
        }else {
            return doGetObjectFromFactoryBean(factory, beanName);
        }
    }

    private Object doGetObjectFromFactoryBean(FactoryBean<?> factory, String beanName) {
        try {
            return factory.getObject();
        } catch (Exception e) {
            throw new RuntimeException("Factory Bean 制造对象失败");
        }
    }
}
