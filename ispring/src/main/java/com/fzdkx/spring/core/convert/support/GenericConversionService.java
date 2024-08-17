package com.fzdkx.spring.core.convert.support;

import com.fzdkx.spring.core.convert.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 * 通用的转换服务，提供基础功能
 *  |-- 判断是否可以转换
 *  |-- 根据 T与S ，获取key，再获取 GenericConverter 进行转换
 *  |-- 向容器中添加 key - GenericConverter
 */
public class GenericConversionService implements ConversionService, ConverterRegistry {

    private Map<GenericConverter.ConvertiblePair, GenericConverter> converters = new HashMap<>();

    @Override
    // 是否可以进行转换
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        // 根据 T 与 S 获取 转换器
        GenericConverter converter = getConverter(sourceType, targetType);
        return converter != null;
    }

    @Override
    // 进行转换
    public <T> T convert(Object source, Class<T> targetType) {
        Class<?> sourceType = source.getClass();
        // 创建通用转换器
        GenericConverter converter = getConverter(sourceType, targetType);
        // 进行转换
        return (T) converter.convert(source, sourceType, targetType);
    }

    @Override
    // 添加转换器
    public void addConverter(Converter<?, ?> converter) {
        // 获取key
        GenericConverter.ConvertiblePair key = getRequiredTypeInfo(converter);
        // 创建适配器
        ConverterAdapter adapter = new ConverterAdapter(key, converter);
        // 添加 key 与 适配器
        converters.put(key, adapter);
    }

    @Override
    public void addConverterFactory(ConverterFactory<?, ?> converterFactory) {
        // 获取key
        GenericConverter.ConvertiblePair key = getRequiredTypeInfo(converterFactory);
        ConverterFactoryAdapter adapter = new ConverterFactoryAdapter(key, converterFactory);
        converters.put(key, adapter);
    }

    @Override
    public void addConverter(GenericConverter converter) {
        converters.put(converter.getConvertiblePair(), converter);
    }


    private GenericConverter getConverter(Class<?> sourceType, Class<?> targetType) {
        List<Class<?>> sourceList = getClassHierarchy(sourceType);
        List<Class<?>> targetList = getClassHierarchy(targetType);
        // 遍历
        for (Class<?> t : targetList) {
            for (Class<?> s : sourceList) {
                // 创建key
                GenericConverter.ConvertiblePair key = new GenericConverter.ConvertiblePair(s, t);
                // 看一下是否有对应的转换器，value
                GenericConverter converter = converters.get(key);
                // 如果有。返回
                if (converter != null) {
                    return converter;
                }
            }
        }
        // 没有
        return null;
    }


    // 获取类的层次结构
    private List<Class<?>> getClassHierarchy(Class<?> clazz) {
        List<Class<?>> hierarchy = new ArrayList<>();
        // 死循环
        while (clazz != null) {
            // 添加
            hierarchy.add(clazz);
            // 获取父类
            clazz = clazz.getSuperclass();
        }
        return hierarchy;
    }


    // 获取所需的类型信息
    private GenericConverter.ConvertiblePair getRequiredTypeInfo(Object object) {
        // 获取接口
        Type[] types = object.getClass().getGenericInterfaces();
        // 获取第一个泛型
        ParameterizedType parameterized = (ParameterizedType) types[0];
        // 获取泛型的实际类型
        Type[] actualTypeArguments = parameterized.getActualTypeArguments();
        // 0 是 sourceType
        Class sourceType = (Class) actualTypeArguments[0];
        // 1 是 sourceType
        Class targetType = (Class) actualTypeArguments[1];
        // 创建一个key返回
        return new GenericConverter.ConvertiblePair(sourceType, targetType);
    }


    // 转换器适配器
    private final class ConverterAdapter implements GenericConverter {

        // key
        private final ConvertiblePair typeInfo;

        // 转换器
        private final Converter<Object, Object> converter;

        public ConverterAdapter(ConvertiblePair typeInfo, Converter<?, ?> converter) {
            this.typeInfo = typeInfo;
            this.converter = (Converter<Object, Object>) converter;
        }

        @Override
        public ConvertiblePair getConvertiblePair() {
            // 获取key
            return typeInfo;
        }

        @Override
        public Object convert(Object source, Class sourceType, Class targetType) {
            return converter.convert(source);
        }
    }


    // 抓换去工厂适配器
    private final class ConverterFactoryAdapter implements GenericConverter {

        // key
        private final ConvertiblePair typeInfo;

        // 转换器工厂
        private final ConverterFactory<Object, Object> converterFactory;

        public ConverterFactoryAdapter(ConvertiblePair typeInfo, ConverterFactory<?, ?> converterFactory) {
            this.typeInfo = typeInfo;
            this.converterFactory = (ConverterFactory<Object, Object>) converterFactory;
        }

        @Override
        public ConvertiblePair getConvertiblePair() {
            // 获取key
            return typeInfo;
        }

        @Override
        public Object convert(Object source, Class sourceType, Class targetType) {
            // 获取对应转换器，进行转换
            return converterFactory.getConverter(targetType).convert(source);
        }
    }

}
