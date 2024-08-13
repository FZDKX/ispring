package com.fzdkx.spring.beans.exception;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 */
public class NoSuchBeanDefinitionException extends BeansException{
    public NoSuchBeanDefinitionException(String msg) {
        super(msg);
    }
}
