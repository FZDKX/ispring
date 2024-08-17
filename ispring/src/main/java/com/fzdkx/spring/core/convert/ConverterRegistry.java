package com.fzdkx.spring.core.convert;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 * 类型转换注册接口
 */
public interface ConverterRegistry {
    void addConverter(Converter<?, ?> converter);

    void addConverter(GenericConverter converter);

    void addConverterFactory(ConverterFactory<?, ?> converterFactory);
}
