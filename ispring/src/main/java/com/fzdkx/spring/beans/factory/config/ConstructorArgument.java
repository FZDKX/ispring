package com.fzdkx.spring.beans.factory.config;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 */
@Getter
public class ConstructorArgument {
    // 索引 -- 值
    private final Map<Integer, ValueHolder> indexedArgumentValues = new LinkedHashMap<>();


    public boolean isEmpty() {
        return this.indexedArgumentValues.isEmpty();
    }

    public Map<Integer, ValueHolder> getIndexedArgumentValues() {
        return indexedArgumentValues;
    }

    public void setValueHolder(Integer index, ValueHolder valueHolder) {
        indexedArgumentValues.put(index, valueHolder);
    }

    public static class ValueHolder {

        // 参数类型
        private Class<?> type;

        /*
            参数值
               |-- 可以是 BeanReference，代表需要从容器中找Bean
               |-- 可以是 其他类型，代表直接赋值
         */
        private Object value;

        public ValueHolder() {

        }

        public ValueHolder(Class<?> type, Object value) {
            this.type = type;
            this.value = value;
        }

        public Class<?> getType() {
            return type;
        }

        public void setType(Class<?> type) {
            this.type = type;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
