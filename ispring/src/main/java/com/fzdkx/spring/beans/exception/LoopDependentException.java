package com.fzdkx.spring.beans.exception;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 */
public class LoopDependentException extends BeansException{
    public LoopDependentException(String msg) {
        super(msg);
    }
}
