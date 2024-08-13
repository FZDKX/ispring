package com.fzdkx.spring.beans.factory.support;

import com.fzdkx.spring.beans.factory.DisposableBean;
import com.fzdkx.spring.beans.factory.config.BeanDefinition;
import com.fzdkx.spring.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 * bean的销毁工作执行流程
 */
public class DisposableBeanAdapter implements DisposableBean {
    private final Object bean;
    private final String beanName;
    private final String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }

    @Override
    public void destroy() throws Exception {
        // 如果bean实现了 DisposableBean接口，那么调用它的 destroy方法
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }
        // 如果bean有自定义的 destroy-method，那么进行调用
        // 但是需要避免 调用两次 destroy()
        if (!StringUtils.isEmpty(destroyMethodName) &&
                !(bean instanceof DisposableBean && "destroy".equals(destroyMethodName))) {
            Method destroyMethod = null;
            try {
                destroyMethod = bean.getClass().getMethod(destroyMethodName);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException(destroyMethodName + "方法未找到");
            }
            destroyMethod.invoke(bean);
        }
    }
}
