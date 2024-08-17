package com.fzdkx.spring.core.convert;


/**
 * @author 发着呆看星
 * @create 2024/8/17
 * 类型转换抽象接口
 */
public interface ConversionService {


    // 是否有转换器
    boolean canConvert(Class<?> sourceType, Class<?> targetType);

    // 进行抓换
    <T> T convert(Object source, Class<T> targetType);
}
