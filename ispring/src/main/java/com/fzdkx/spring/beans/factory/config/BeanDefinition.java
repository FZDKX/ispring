package com.fzdkx.spring.beans.factory.config;


import lombok.Data;

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

    public void setIsFactoryBean(boolean flag){
        this.isFactoryBean = flag;
    }

}
