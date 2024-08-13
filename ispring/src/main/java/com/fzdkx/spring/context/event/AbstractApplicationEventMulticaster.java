package com.fzdkx.spring.context.event;

import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.BeanFactory;
import com.fzdkx.spring.beans.factory.BeanFactoryAware;
import com.fzdkx.spring.context.ApplicationEvent;
import com.fzdkx.spring.context.ApplicationListener;
import com.fzdkx.spring.util.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * 抽象的事件广播器，对事件的基本操作进行了实现
 */
public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    // 事件监听器集合
    public final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();

    // 通过Aware接口，获取一个上下文对象
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    // 添加监听器
    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add(listener);
    }

    // 移除事件
    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove(listener);
    }

    // 通过事件，获取对应监听器集合
    protected Collection<ApplicationListener<?>> getApplicationListeners(ApplicationEvent event) {
        LinkedList<ApplicationListener<?>> listeners = new LinkedList<>();
        for (ApplicationListener<?> listener : applicationListeners) {
            if (supportsEvent(listener, event)) {
                listeners.add(listener);
            }
        }
        return listeners;
    }

    // 判断监听器是否堆该事件感兴趣
    protected boolean supportsEvent(ApplicationListener<?> listener, ApplicationEvent event) {
        Class<? extends ApplicationListener> aClass = listener.getClass();
        // 判断该监听器是否是CGLIB生成的类，如果是，获取其父类(CGLIB基于继承，代理类是子类，原始类是父类)，也就是原始类
        // 如果不是，直接获取
        Class<?> targetClass = ClassUtils.isCglibProxyClass(aClass) ? aClass.getSuperclass() : aClass;
        // 获取该类实现的接口，也就是 ApplicationListener<?> 接口
        Type genericInterface = targetClass.getGenericInterfaces()[0];
        // 获取 ApplicationListener<?>接口泛型的实际产参数类型
        Type actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
        // 获取该类型的全类名
        String className = actualTypeArgument.getTypeName();
        Class<?> eventClassName;
        try {
            // 反射获取 实际泛型类型 的 Class
            eventClassName = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new BeansException("wrong event class name: " + className);
        }
        // 如果 event 等于 或 继承 或 实现  ApplicationListener<?> 中 的 ?
        // 那么代表 这个ApplicationListener 可以监听 当前的 event事件
        return eventClassName.isAssignableFrom(event.getClass());
    }
}
