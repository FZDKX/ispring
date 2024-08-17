package com.fzdkx.spring.core.convert;

import java.util.Objects;
import java.util.Set;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 * 通用的转换器
 */
public interface GenericConverter {

    // 获取 T--S 的转换器
    ConvertiblePair getConvertiblePair();

    /**
     * 进行转换
     * @param source 源对象
     * @param sourceType 源对象类型
     * @param targetType 目标对象
     * @return 返回转换后的对象
     */
    Object convert(Object source, Class<?> sourceType, Class<?> targetType);

    // 其实就是一个 key
    // 使用 sourceType 和 targetType 组合从一个对象，来作为一个key
    // 可以通过这个key，获取对应的转换器
    final class ConvertiblePair {

        // 原对象类型
        private final Class<?> sourceType;

        // 目标对象类型
        private final Class<?> targetType;

        public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        public Class<?> getSourceType() {
            return sourceType;
        }

        public Class<?> getTargetType() {
            return targetType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ConvertiblePair that = (ConvertiblePair) o;
            return Objects.equals(sourceType, that.sourceType) && Objects.equals(targetType, that.targetType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sourceType, targetType);
        }
    }
}
