package com.fzdkx.spring.beans.factory.support;

import com.fzdkx.spring.beans.exception.BeanDefinitionStoreException;
import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.exception.NoSuchBeanDefinitionException;
import com.fzdkx.spring.beans.factory.ConfigurableListableBeanFactory;
import com.fzdkx.spring.beans.factory.config.BeanDefinition;
import com.fzdkx.spring.beans.factory.config.BeanPostProcessor;
import com.fzdkx.spring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.fzdkx.spring.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 发着呆看星
 * @create 2024/8/10
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

    // 单例beanDefinition
    private final Map<String, BeanDefinition> singletonBeanDefinitionMap = new ConcurrentHashMap<>(256);

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        if (beanDefinition == null || StringUtils.isEmpty(beanName)) {
            return;
        }
        // 如果已有，不允许存在同名bean
        if (beanDefinitionMap.containsKey(beanName)) {
            throw new BeanDefinitionStoreException("定义了同名 bean：" + beanName);
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
            singletonBeanDefinitionMap.put(beanName, beanDefinition);
        }
        beanDefinitionNames.add(beanName);
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        String simpleName = beanDefinition.getBeanClass().getSimpleName();
        String beanName = StringUtils.lowerFirst(simpleName);
        registerBeanDefinition(beanName, beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        if (beanDefinition == null) {
            return;
        }
        List<String> list = allBeanNamesByType.get(beanDefinition.getBeanClass());
        list.remove(beanName);
        if (beanDefinition.isSingleton()) {
            list = singletonBeanNamesByType.get(beanDefinition.getBeanClass());
            list.remove(beanName);
        }
        beanDefinitionMap.remove(beanName);
        beanDefinitionNames.remove(beanName);
        singletonBeanDefinitionMap.remove(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public void preInstantiateSingletons() throws BeansException {
        // 运行BeanPostProcess进行准备工作
        prepareBeanPostProcess();
        // 普通bean初始化
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
    public List<BeanDefinition> getBeanDefinitionByType(Class<?> type) {
        List<BeanDefinition> list = new ArrayList<>();
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            Class<?> clazz = entry.getValue().getBeanClass();
            // 判断 type 是否是 beanClass 的 同类 或 父类 或 接口
            if (type.isAssignableFrom(clazz)) {
                // 如果是，加入
                list.add(entry.getValue());
            }
        }
        return list;
    }

    private void prepareBeanPostProcess() {
        for (BeanPostProcessor bpp : getBeanPostProcessors()) {
            if (bpp instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor ibpp = (InstantiationAwareBeanPostProcessor) bpp;
                ibpp.prepareInstantiationAware();
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
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            Class<?> clazz = entry.getValue().getBeanClass();
            // 判断 type 是否是 beanClass 的 同类 或 父类 或 接口
            if (type.isAssignableFrom(clazz)) {
                // 如果没有，会调用getBean方法创建
                map.put(entry.getKey(), (T) getBean(entry.getKey()));
            }
        }
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
    public List<String> getBeanNamesOfType(Class<Object> type) {
        ArrayList<String> list = new ArrayList<>();
        beanDefinitionMap.forEach((beanName, deanDefinition) -> {
            Class<?> clazz = deanDefinition.getBeanClass();
            // 判断 type 是否是 beanClass 的 同类 或 父类 或 接口
            if (type.isAssignableFrom(clazz)) {
                // 如果没有，会调用getBean方法创建
                list.add(beanName);
            }
        });
        return list;
    }

    @Override
    public Set<BeanDefinition> getClassByAnnotation(Class<? extends Annotation> clazz) {
        Set<BeanDefinition> set = new HashSet<>();
        // 获取所有的单例bean
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            Class<?> beanClass = entry.getValue().getBeanClass();
            if (beanClass.isAnnotationPresent(clazz)) {
                set.add(entry.getValue());
            }
        }
        return set;
    }
}
