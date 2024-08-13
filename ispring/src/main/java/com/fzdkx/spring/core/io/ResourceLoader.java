package com.fzdkx.spring.core.io;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 */
public interface ResourceLoader {

    String CLASSPATH_URL_PREFIX = "classpath:";

    // 获取资源

    Resource getResource(String location);
}
