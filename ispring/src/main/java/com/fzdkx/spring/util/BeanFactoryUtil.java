package com.fzdkx.spring.util;

import com.fzdkx.spring.beans.factory.BeanFactory;
import com.sun.istack.internal.Nullable;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 */
public class BeanFactoryUtil {
    public static boolean isFactoryDereference(@Nullable String name) {
        return (name != null && name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX));
    }
}
