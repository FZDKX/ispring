package com.fzdkx.spring.test.factyory_bean;

import com.fzdkx.spring.beans.factory.FactoryBean;
import com.fzdkx.spring.context.annotation.Component;
import com.fzdkx.spring.test.live.MyBean;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 */
@Slf4j
@Component("myBean")
public class MyFactoryBean implements FactoryBean<MyBean> {
    @Override
    public MyBean getObject() throws Exception {
        log.debug("getObject 被调用");
        return new MyBean();
    }

    @Override
    public Class<?> getObjectType() {
        return MyBean.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
