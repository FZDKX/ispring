package com.fzdkx.spring.beans.factory;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 * 销毁Bean接口
 */
public interface DisposableBean {

    // 在销毁Bean之前，调用该方法
    void destroy() throws Exception;

}
