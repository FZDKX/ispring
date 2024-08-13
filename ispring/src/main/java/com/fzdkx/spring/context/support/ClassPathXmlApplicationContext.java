package com.fzdkx.spring.context.support;

import com.fzdkx.spring.beans.exception.BeansException;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext{
    private String[] locations;

    public ClassPathXmlApplicationContext() {
    }

    public ClassPathXmlApplicationContext(String location) {
        this(new String[] {location});
    }

    public ClassPathXmlApplicationContext(String[] locations) throws BeansException {
        this.locations = locations;
        refresh();
    }

    @Override
    protected String[] getConfigLocations() {
        return this.locations;
    }
}
