package com.fzdkx.spring.context.annotation;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.ConfigurableListableBeanFactory;
import com.fzdkx.spring.beans.factory.config.BeanDefinition;
import com.fzdkx.spring.beans.factory.config.ConfigurableBeanFactory;
import com.fzdkx.spring.beans.factory.support.BeanDefinitionRegistry;
import com.fzdkx.spring.beans.factory.support.DefaultListableBeanFactory;
import com.fzdkx.spring.util.AnnotationUtils;
import com.fzdkx.spring.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 发着呆看星
 * @create 2024/8/15
 * 包扫描注册BeanDefinition
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    private ConfigurableListableBeanFactory beanFactory;

    private final BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    // 扫描
    public void doScan(String... packages) {
        for (String p : packages) {
            Set<Class<?>> classes = findCandidateComponents(p);
            for (Class<?> clazz : classes) {
                Configuration configuration = clazz.getAnnotation(Configuration.class);
                // 如果是配置类
                if (configuration != null) {
                    registerConfig(clazz);
                }
                // 如果普通Bean
                else {
                    registryNormal(clazz);
                }
            }
        }
    }

    private void registryNormal(Class<?> clazz) {
        // 创建BeanDefinition
        BeanDefinition beanDefinition = new BeanDefinition(clazz);
        // 解析 beanName
        String beanName = parseBeanName(clazz);
        // 解析 scope
        String scope = parseScope(clazz);
        // 解析 lazy
        boolean isLazy = parseLazy(clazz);
        beanDefinition.setName(beanName);
        beanDefinition.setScope(scope);
        beanDefinition.setLazyInit(isLazy);
        // 注册BeanDefinition
        registry.registerBeanDefinition(beanName, beanDefinition);
        // 如果有 PropertiesSource
        addProperties(clazz);
    }

    private boolean parseLazy(Class<?> clazz) {
        Lazy lazy = (Lazy) AnnotationUtils.getAnnotation(clazz, Lazy.class);
        if (lazy == null) {
            return false;
        } else {
            return lazy.value();
        }
    }

    private String parseScope(Class<?> clazz) {
        Scope scope = (Scope) AnnotationUtils.getAnnotation(clazz, Scope.class);
        if (scope == null) {
            return ConfigurableBeanFactory.SCOPE_SINGLETON;
        }
        String value = scope.value();
        if (!StringUtils.isEmpty(value)) {
            if (ConfigurableBeanFactory.SCOPE_PROTOTYPE.equals(value)) {
                value = ConfigurableBeanFactory.SCOPE_PROTOTYPE;
            } else if (ConfigurableBeanFactory.SCOPE_SINGLETON.equals(value)) {
                value = ConfigurableBeanFactory.SCOPE_SINGLETON;
            } else {
                throw new BeansException("bean 作用域设置出错");
            }
        }
        return value;
    }

    private String parseBeanName(Class<?> clazz) {
        Component component = (Component) AnnotationUtils.getAnnotation(clazz, Component.class);
        if (component == null) {
            throw new BeansException("bean 解析异常");
        }
        String beanName = component.value();
        if (StringUtils.isEmpty(beanName)) {
            beanName = StringUtils.lowerFirst(clazz.getSimpleName());
        }
        return beanName;
    }

    // 注册配置类
    public void registerConfig(Class<?>... componentClasses) {
        for (Class<?> componentClass : componentClasses) {
            registerConfig(componentClass);
        }
    }

    public void registerConfig(Class<?> beanClass) {
        doRegisterConfig(beanClass);
    }

    private <T> void doRegisterConfig(Class<T> beanClass) {
        // 创建Bean定义信息
        BeanDefinition bd = new BeanDefinition(beanClass);
        // 解析 lazy 和 scope 注解
        parserLazyAndScope(beanClass, bd);
        // 判断是否是@Configuration
        Configuration configuration = beanClass.getAnnotation(Configuration.class);
        String beanName = StringUtils.lowerFirst(beanClass.getSimpleName());
        // 是一个配置类
        if (configuration != null) {
            // 解析bean name
            String value = configuration.value();
            if (!StringUtils.isEmpty(value)) {
                beanName = value;
            }
            // 是配置类，进行标识
            bd.setConfiguration(true);
            // 解析配置类的@Bean
            parserBean(beanClass, beanName);
            // 解析@ComponentScan
            ComponentScan componentScan = beanClass.getAnnotation(ComponentScan.class);
            if (componentScan != null) {
                // 获取包名
                String[] basePackages = componentScan.value();
                doScan(basePackages);
            }
        }
        // 解析完成，注册Bean
        bd.setName(beanName);
        // 注册
        registry.registerBeanDefinition(beanName, bd);
        // 如果有 PropertiesSource
        addProperties(beanClass);
    }

    private void parserLazyAndScope(Class<?> beanClass, BeanDefinition bd) {
        // 解析@Scope
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (scope != null) {
            bd.setScope(scope.value());
        }
        // 解析@Lazy
        Lazy lazy = beanClass.getAnnotation(Lazy.class);
        if (lazy != null) {
            bd.setLazyInit(lazy.value());
        }
    }

    public void parserBean(Class<?> configuration, String configurationName) {
        Method[] methods = configuration.getDeclaredMethods();
        // 获取所有加上@Bean注解的方法
        for (Method method : methods) {
            Bean bean = method.getAnnotation(Bean.class);
            if (bean == null) {
                continue;
            }
            Class<?> returnType = method.getReturnType();
            BeanDefinition bd = new BeanDefinition(returnType);
            // 解析 lazy 和 scope 注解
            parserLazyAndScope(method.getClass(), bd);
            // 解析 bean 注解
            // bean name
            String beanName = StringUtils.lowerFirst(method.getName());
            String value = bean.value();
            if (!StringUtils.isEmpty(value)) {
                beanName = value;
            }
            bd.setName(beanName);
            // initMethod
            String initMethod = bean.initMethod();
            if (!StringUtils.isEmpty(initMethod)) {
                bd.setInitMethodName(initMethod);
            }
            // destroyMethod
            String destroyMethod = bean.destroyMethod();
            if (!StringUtils.isEmpty(destroyMethod)) {
                bd.setInitMethodName(destroyMethod);
            }
            // 设置为方法创建，并设置创建方法
            bd.setMethodCreate(true);
            bd.setCreateMethod(method);
            bd.setConfigurationName(configurationName);
            // 注册BeanDefinition
            this.registry.registerBeanDefinition(beanName, bd);
        }
    }

    public void addProperties(Class<?> clazz){
        // 如果有 PropertiesSource
        PropertiesSource propertiesSource = clazz.getAnnotation(PropertiesSource.class);
        if (propertiesSource != null) {
            String[] value = propertiesSource.value();
            if (value != null) {
                Collections.addAll(beanFactory.getLocations(), value);
            }
        }
    }

    public void setBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
