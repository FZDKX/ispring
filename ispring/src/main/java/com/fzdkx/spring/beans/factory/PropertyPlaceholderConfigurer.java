package com.fzdkx.spring.beans.factory;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.config.BeanDefinition;
import com.fzdkx.spring.beans.factory.config.BeanFactoryPostProcessor;
import com.fzdkx.spring.beans.factory.config.PropertyValue;
import com.fzdkx.spring.beans.factory.config.PropertyValues;
import com.fzdkx.spring.core.io.DefaultResourceLoader;
import com.fzdkx.spring.core.io.Resource;
import com.fzdkx.spring.util.StringUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author 发着呆看星
 * @create 2024/8/15
 * 对占位符 ${}进行处理
 */
public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    // 前缀
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    // 后缀
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    // 文件路径
    private String location;

    @Override
    //  加载 properties 配置文件，如果BeanDefinition的PropertyValue中含有占位符，替换
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            // 加载 properties 配置文件
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(location);
            Properties properties = new Properties();
            properties.load(resource.getInputStream());
            // 获取所有的BeanDefinition
            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for (String beanName : beanDefinitionNames) {
                // 获取beanDefinition
                BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
                // 对属性值进行占位符替换
                PropertyValues propertyValues = bd.getPropertyValues();
                for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                    Object value = propertyValue.getValue();
                    if (!(value instanceof String)) {
                        continue;
                    }
                    // 是字符串，判断是否含有 占位符
                    StringBuilder sb = new StringBuilder((String) value);
                    // 获取前缀索引
                    int start = sb.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
                    // 获取后缀索引
                    int end = sb.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
                    // 如果含有，替换
                    if (start != -1 && end != -1 && start < end) {
                        // 获取key
                        String key = sb.substring(start + 2, end);
                        // 从properties中获取value
                        String property = properties.getProperty(key);
                        // 没有，报错
                        if (StringUtils.isEmpty(property)) {
                            throw new BeansException("properties中无：" + key);
                        }
                        // 有，替换
                        sb.replace(start, end + 1, property);
                        propertyValue.setValue(sb.toString());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("没有找到配置文件：" + location);
        }
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
