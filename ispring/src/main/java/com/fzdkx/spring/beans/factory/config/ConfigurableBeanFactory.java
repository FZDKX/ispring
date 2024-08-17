package com.fzdkx.spring.beans.factory.config;

import com.fzdkx.spring.beans.factory.HierarchicalBeanFactory;
import com.fzdkx.spring.core.convert.ConversionService;
import com.fzdkx.spring.util.StringValueResolver;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 * 可配置的Bean工厂：可对 BeanPostProcessor 进行操作
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";


    // 添加 BeanPostProcessor
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);


    // 销毁 单例Bean
    void destroySingletons();


    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    String resolveEmbeddedValue(String value);

    // 获取转换器
    ConversionService getConversionService();

    // 设置转换服务
    void setConversionService(ConversionService conversionService);
}