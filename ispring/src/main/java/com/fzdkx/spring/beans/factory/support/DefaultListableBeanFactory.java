package com.fzdkx.spring.beans.factory.support;

import com.fzdkx.spring.beans.factory.ConfigurableListableBeanFactory;
import com.fzdkx.spring.beans.factory.config.BeanDefinition;
import com.fzdkx.spring.beans.exception.BeanDefinitionStoreException;
import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.exception.NoSuchBeanDefinitionException;
import com.fzdkx.spring.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 *
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {

    // name -> BeanDefinition
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    // names
    private volatile List<String> beanDefinitionNames = new ArrayList<>(256);

    // 所有 class -> names
    private final Map<Class<?>, List<String>> allBeanNamesByType = new ConcurrentHashMap<>(64);

    // 单例 class -> names
    private final Map<Class<?>, List<String>> singletonBeanNamesByType = new ConcurrentHashMap<>(64);

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        if (beanDefinition == null || StringUtils.isEmpty(beanName)) {
            return;
        }
        // 添加
        List<String> nameList = allBeanNamesByType.getOrDefault(beanDefinition.getBeanClass(), new ArrayList<>());
        nameList.add(beanName);
        allBeanNamesByType.putIfAbsent(beanDefinition.getBeanClass(), nameList);
        // 如果是单例
        if (beanDefinition.isSingleton()) {
            nameList = singletonBeanNamesByType.getOrDefault(beanDefinition.getBeanClass(), new ArrayList<>());
            nameList.add(beanName);
            singletonBeanNamesByType.putIfAbsent(beanDefinition.getBeanClass(), nameList);
        }
        beanDefinitionNames.add(beanName);
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        if (beanDefinition == null) {
            return;
        }
        beanDefinitionNames.remove(beanName);
        List<String> list = allBeanNamesByType.get(beanName);
        list.remove(beanName);
        if (beanDefinition.isSingleton()) {
            list = singletonBeanNamesByType.get(beanName);
            list.remove(beanName);
        }
        beanDefinitionMap.remove(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public void preInstantiateSingletons() throws BeansException {
        ArrayList<String> names = new ArrayList<>(beanDefinitionNames);
        for (String name : names) {
            BeanDefinition bd = getBeanDefinition(name);
            if (bd.isSingleton() && !bd.isLazyInit()) {
                // 如果是FactoryBean
                if (bd.isFactoryBean()) {
                    // &name
                    getBean(FACTORY_BEAN_PREFIX + name);
                } else {
                    getBean(name);
                }
            }
        }
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        HashMap<String, T> map = new HashMap<>();
        beanDefinitionMap.forEach((beanName, deanDefinition) -> {
            Class<?> clazz = deanDefinition.getBeanClass();
            // 如果 clazz 继承了type 或 实现了type ，返回true
            if (type.isAssignableFrom(clazz)) {
                // 创建Bean，并存在map中
                map.put(beanName, (T) getBean(beanName));
            }
        });
        return map;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        int size = beanDefinitionNames.size();
        String[] arr = new String[size];
        int i = 0;
        for (String beanDefinitionName : beanDefinitionNames) {
            arr[i++] = beanDefinitionName;
        }
        return arr;
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitionNames.size();
    }

}
