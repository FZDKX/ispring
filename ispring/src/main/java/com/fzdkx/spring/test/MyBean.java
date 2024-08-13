package com.fzdkx.spring.test;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 */
@Slf4j
public class MyBean implements BeanFactoryAware , BeanNameAware , BeanClassLoaderAware ,
                                InitializingBean , DisposableBean {
    public MyBean() {
        log.debug("create myBean");
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        log.debug("myBean get classLoader：" + classLoader);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        log.debug("myBean get beanFactory：" + beanFactory);
    }

    @Override
    public void setBeanName(String beanName) {
        log.debug("myBean get beanName：" + beanName);
    }

    @Override
    public void destroy() throws Exception {
        log.debug("myBean 系统 destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("myBean 系统 init");
    }

    // 自定义 init
    public void sayHello() {
        log.debug("myBean 自定义 init");
    }

    // 自定义 destroy
    public void sayBye() {
        log.debug("myBean 自定义 destroy");
    }
}
