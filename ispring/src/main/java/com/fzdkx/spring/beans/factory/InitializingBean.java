package com.fzdkx.spring.beans.factory;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 * 初始化Bean接口
 */
public interface InitializingBean {

    // 在Bean属性填充之后调用
    void afterPropertiesSet() throws Exception;

}
