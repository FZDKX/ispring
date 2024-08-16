package com.fzdkx.spring.beans.factory.config;


import lombok.Data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 * Bean定义信息
 */
@Data
public class BeanDefinition {
    String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

    String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;

    private String name;

    private String beanClassName;

    private Class<?> beanClass;

    private String scope = SCOPE_SINGLETON;

    private boolean isLazyInit = false;

    private String initMethodName;

    private String destroyMethodName;

    private PropertyValues propertyValues;

    private ConstructorArgument constructorArgument;

    private boolean isFactoryBean = false;

    public BeanDefinition() {
    }

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public boolean hasConstructorArgumentValues() {
        return constructorArgument != null && !constructorArgument.isEmpty();
    }

    public boolean hasPropertyValues() {
        return propertyValues != null && !propertyValues.isEmpty();
    }

    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(scope);
    }

    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(scope);
    }

    public void setIsFactoryBean(boolean flag) {
        this.isFactoryBean = flag;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeanClassName() {
        return beanClass.getName();
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isLazyInit() {
        return isLazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        isLazyInit = lazyInit;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public PropertyValues getPropertyValues() {
        if (propertyValues == null){
            this.propertyValues = new PropertyValues();
        }
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public ConstructorArgument getConstructorArgument() {
        if (constructorArgument != null) {
            return constructorArgument;
        } else {
            // 为空
            // 判断是否只有一个构造函数
            if (beanClass.getDeclaredConstructors().length == 1) {
                Constructor<?> constructor = beanClass.getDeclaredConstructors()[0];
                Class<?>[] types = constructor.getParameterTypes();
                Parameter[] parameters = constructor.getParameters();
                ConstructorArgument argument = new ConstructorArgument();
                for (int i = 0; i < types.length; i++) {
                    int j = i;
                    argument.setValueHolder(i, new ConstructorArgument.ValueHolder(types[i], (BeanReference) () -> parameters[j].getName()));
                }
                this.constructorArgument = argument;
            }
            return constructorArgument;
        }
    }

    public void setConstructorArgument(ConstructorArgument constructorArgument) {
        this.constructorArgument = constructorArgument;
    }

    public boolean isFactoryBean() {
        return isFactoryBean;
    }

    public void setFactoryBean(boolean factoryBean) {
        isFactoryBean = factoryBean;
    }

}
