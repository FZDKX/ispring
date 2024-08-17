package com.fzdkx.spring.aop.framework.autoproxy;

import com.fzdkx.spring.aop.AdvisedSupport;
import com.fzdkx.spring.aop.ClassFilter;
import com.fzdkx.spring.aop.DefaultAspect;
import com.fzdkx.spring.aop.TargetSource;
import com.fzdkx.spring.aop.annotation.Aspect;
import com.fzdkx.spring.aop.aspectj.AspectInfo;
import com.fzdkx.spring.aop.aspectj.AspectJExpressionPointcut;
import com.fzdkx.spring.aop.aspectj.AspectListExecutor;
import com.fzdkx.spring.aop.framework.ProxyFactory;
import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.BeanFactory;
import com.fzdkx.spring.beans.factory.BeanFactoryAware;
import com.fzdkx.spring.beans.factory.ConfigurableListableBeanFactory;
import com.fzdkx.spring.beans.factory.FactoryBean;
import com.fzdkx.spring.beans.factory.config.BeanDefinition;
import com.fzdkx.spring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.fzdkx.spring.beans.factory.support.DefaultListableBeanFactory;
import com.fzdkx.spring.context.annotation.Order;
import com.fzdkx.spring.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * 融入Bean生命周期的核心：自动代理创建者，是一个Bean后置处理器
 */
public class DefaultAdvisorAutoProxyCreator extends InstantiationAwareBeanPostProcessor implements BeanFactoryAware {

    public static final String DEFAULT_NAME = "defaultAdvisorAutoProxyCreator";

    private ConfigurableListableBeanFactory beanFactory;

    // advisor 集合
    private volatile Set<AspectInfo> advisors;

    // 当调用
    private final Map<Object, Object> earlyProxyReferences = new ConcurrentHashMap<>(16);

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public void prepareInstantiationAware() {
        // 获取所有的Advisor
        loadAdvisors();
    }

    // 创建代理对象
    @Override
    public Object getEarlyBeanReference(Object bean, String beanName, BeanDefinition bd) {
        // 获取key
        Object cacheKey = getCacheKey(bean.getClass(), beanName);
        // key + 原始bean
        this.earlyProxyReferences.put(cacheKey, bean);
        // 创建代理bean返回
        return wrapIfNecessary(bean, bd);
    }

    private void loadAdvisors() {
        if (advisors == null) {
            synchronized (this) {
                if (advisors == null) {
                    advisors = new HashSet<>();
                    // 获取 容器中 所有的标注了@Aspect的类
                    Set<BeanDefinition> classSet = this.beanFactory.getClassByAnnotation(Aspect.class);
                    for (BeanDefinition bd : classSet) {
                        // 验证规则 ，使用@Aspect 必须 继承 DefaultAspect
                        if (!verifyAspect(bd.getBeanClass())) {
                            throw new BeansException("bean 配置错误：" + bd.getBeanClass());
                        }
                        categorizeAspect(bd);
                    }
                }
            }
        }
    }

    private void categorizeAspect(BeanDefinition bd) {
        Class<?> clazz = bd.getBeanClass();
        // 创建AspectInfo
        int orderValue = 10;
        Order order = clazz.getAnnotation(Order.class);
        if (order != null) {
            orderValue = order.value();
        }
        // 获取bean
        DefaultAspect aspectObject = (DefaultAspect) this.beanFactory.getBean(bd.getName());
        Aspect aspect = clazz.getAnnotation(Aspect.class);
        // 获取expression
        String expression = aspect.value();
        if (StringUtils.isEmpty(expression)) {
            throw new BeansException("aop配置错误：" + clazz);
        }
        // 填充map
        advisors.add(new AspectInfo(orderValue, aspectObject, expression));
    }


    private boolean verifyAspect(Class<?> clazz) {
        return DefaultAspect.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(Aspect.class);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    // 在后置方法中创建代理，在bean初始化完成之后
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean != null) {
            // 获取key
            Object cacheKey = getCacheKey(bean.getClass(), beanName);
            // 获取原始bean ，如果 原始bean == 现在的bean，那么可能需要创建代理
            if (this.earlyProxyReferences.remove(cacheKey) == bean) {
                // 尝试进行代理
                return wrapIfNecessary(bean,beanFactory.getBeanDefinition(beanName));
            }
        }
        // 已经是代理了，无需再次代理
        return bean;
    }

    // 尝试返回代理对象
    public Object wrapIfNecessary(Object bean, BeanDefinition bd) {
        if (advisors == null){
            return bean;
        }
        Object result = bean;
        boolean flag = false;
        // 如果被拦截，进行代理
        AdvisedSupport advisedSupport = new AdvisedSupport();
        ArrayList<AspectInfo> aspectInfos = new ArrayList<>();
        // 遍历
        for (AspectInfo info : advisors) {
            AspectJExpressionPointcut pointcut = info.getPointcut();
            ClassFilter classFilter = pointcut.getClassFilter();
            // 如果当前bean未被拦截，那么不代理
            if (!classFilter.matches(bd.getBeanClass())) {
                continue;
            }
            flag = true;
            // 设置方法拦截器
            aspectInfos.add(info);
        }
        if (flag) {
            // 创建 TargetSource
            TargetSource targetSource = new TargetSource(bean);
            // 设置 TargetSource
            advisedSupport.setTargetSource(targetSource);
            // 创建方法拦截器
            AspectListExecutor executor = new AspectListExecutor(aspectInfos);
            // 设置方法拦截器
            advisedSupport.setMethodInterceptor(executor);
            result = new ProxyFactory(advisedSupport).getObject();
        }
        return result;
    }


    protected Object getCacheKey(Class<?> beanClass, String beanName) {
        if (!StringUtils.isEmpty(beanName)) {
            return (FactoryBean.class.isAssignableFrom(beanClass) ?
                    BeanFactory.FACTORY_BEAN_PREFIX + beanName : beanName);
        } else {
            return beanClass;
        }
    }
}
