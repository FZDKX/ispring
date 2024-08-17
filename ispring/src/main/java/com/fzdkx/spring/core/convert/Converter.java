package com.fzdkx.spring.core.convert;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 * 类型转换接口
 */
public interface Converter<S, T> {
    T convert(S source);
}
