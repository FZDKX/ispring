package com.fzdkx.spring.context;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.Aware;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 * 可以获取 ApplicationContext 进行属性赋值
 */
public interface ApplicationContextAware extends Aware {
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
