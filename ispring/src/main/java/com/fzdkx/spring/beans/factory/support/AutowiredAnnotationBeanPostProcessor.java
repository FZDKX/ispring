package com.fzdkx.spring.beans.factory.support;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.exception.NoSuchBeanDefinitionException;
import com.fzdkx.spring.beans.factory.BeanFactory;
import com.fzdkx.spring.beans.factory.BeanFactoryAware;
import com.fzdkx.spring.beans.factory.ConfigurableListableBeanFactory;
import com.fzdkx.spring.beans.factory.config.*;
import com.fzdkx.spring.context.annotation.Autowired;
import com.fzdkx.spring.context.annotation.Qualifier;
import com.fzdkx.spring.context.annotation.Value;
import com.fzdkx.spring.util.BeanUtils;
import com.fzdkx.spring.util.ClassUtils;
import com.fzdkx.spring.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author 发着呆看星
 * @create 2024/8/16
 * 除了 @Value ，@Autowired 注解的 BeanPostProcess
 */
public class AutowiredAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessor implements BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public BeanDefinition postProcessPropertyValues(BeanDefinition bd, Object bean, String beanName) throws BeansException {
        PropertyValues propertyValues = bd.getPropertyValues();
        // 处理字段上的 @Value注解 和 @Autowired注解
        Class<?> clazz = bean.getClass();
        clazz = ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;
        // 获取所有的字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 获取@Value注解
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (valueAnnotation != null) {
                String value = valueAnnotation.value();
                if (!StringUtils.isEmpty(value)) {
                    // 处理
                    value = beanFactory.resolveEmbeddedValue(value);
                    // 设置字段值
                    BeanUtils.setFieldValue(bean, field, value);
                }
            }
            // 获取@Autowired注解
            Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
            if (autowiredAnnotation != null) {
                // 获取该字段的类型
                Class<?> fieldType = field.getType();
                // 查找bean
                List<BeanDefinition> definitions = beanFactory.getBeanDefinitionByType(fieldType);
                String fieldName = field.getName();
                // 为空
                if (definitions.isEmpty()) {
                    throw new NoSuchBeanDefinitionException("找不到bean：" + fieldType);
                }
                // 如果只有一个，那么获取
                else if (definitions.size() == 1) {
                    // 创建
                    PropertyValue propertyValue = new PropertyValue();
                    propertyValue.setPropertyName(fieldName);
                    propertyValue.setValue((BeanReference) () -> definitions.get(0).getName());
                    // 添加
                    propertyValues.addProperty(propertyValue);
                }
                // 多个
                else {
                    // 是否有 @Qualifier
                    Qualifier qualifier = field.getAnnotation(Qualifier.class);
                    String ref = qualifier != null ? qualifier.value() : fieldName;
                    PropertyValue propertyValue = new PropertyValue();
                    propertyValue.setPropertyName(fieldName);
                    propertyValue.setValue((BeanReference) () -> ref);
                    // 添加
                    propertyValues.addProperty(propertyValue);
                }
            }
        }
        // 处理方法上的 @Autowired
        return bd;
    }
}
