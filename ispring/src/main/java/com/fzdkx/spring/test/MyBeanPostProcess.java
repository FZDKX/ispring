package com.fzdkx.spring.test;

import com.fzdkx.spring.beans.factory.config.BeanPostProcessor;
import com.fzdkx.spring.beans.exception.BeansException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 */
@Slf4j
public class MyBeanPostProcess implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        log.debug(beanName + "   before");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.debug(beanName + "   after");
        return bean;
    }
}
