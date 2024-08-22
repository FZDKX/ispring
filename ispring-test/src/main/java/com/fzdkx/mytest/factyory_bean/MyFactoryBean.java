package com.fzdkx.mytest.factyory_bean;

import com.fzdkx.mytest.live.MyBean;
import com.fzdkx.spring.beans.factory.FactoryBean;
import com.fzdkx.spring.context.annotation.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 */
@Slf4j
@Component("factoryBeanTest")
public class MyFactoryBean implements FactoryBean<FactoryBeanTest> {
    @Override
    public FactoryBeanTest getObject() throws Exception {
        log.debug("getObject 被调用");
        return new FactoryBeanTest();
    }

    @Override
    public Class<?> getObjectType() {
        return FactoryBeanTest.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
