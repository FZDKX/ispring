package com.fzdkx.spring.core.convert;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 * 类型转换工厂
 * S：source，源类型
 * T：target，目标类型
 * T 继承于 R
 *
 */
public interface ConverterFactory<S, R> {

    // 根据目标Target的类型，获取可以转换成Target的工厂
    <T extends R> Converter<S, T> getConverter(Class<T> targetType);
}
