package com.fzdkx.spring.core.convert.support;

import com.fzdkx.spring.core.convert.ConverterRegistry;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 * 默认的转换服务：可以注册转换工厂
 */
public class DefaultConversionService extends GenericConversionService {
    public DefaultConversionService() {
        addDefaultConverters(this);
    }

    public static void addDefaultConverters(ConverterRegistry converterRegistry) {
        // 添加各类类型转换工厂
        converterRegistry.addConverterFactory(new StringToNumberConverterFactory());
    }
}
