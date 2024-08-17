package com.fzdkx.spring.core.convert.support;

import com.fzdkx.spring.beans.factory.config.SimpleDataType;
import com.fzdkx.spring.core.convert.Converter;
import com.fzdkx.spring.core.convert.ConverterFactory;
import com.sun.istack.internal.Nullable;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 * String 转换从 Number
 */
public class StringToNumberConverterFactory implements ConverterFactory<String, Number> {
    @Override
    public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToNumber<>(targetType);
    }

    private static final class StringToNumber<T extends Number> implements Converter<String, T> {

        private final Class<T> targetType;

        public StringToNumber(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        @Nullable
        public T convert(String source) {
            if (source.isEmpty()) {
                return null;
            }
            return SimpleDataType.parseNumber(source, this.targetType);
        }
    }
}
