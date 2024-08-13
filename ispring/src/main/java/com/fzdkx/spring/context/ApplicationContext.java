package com.fzdkx.spring.context;

import com.fzdkx.spring.beans.factory.HierarchicalBeanFactory;
import com.fzdkx.spring.beans.factory.ListableBeanFactory;
import com.fzdkx.spring.core.io.ResourceLoader;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 */
public interface ApplicationContext extends ListableBeanFactory , HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher{
}
