package com.fzdkx.spring.beans.factory.support;

import com.fzdkx.spring.beans.exception.BeanInstanceException;
import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.exception.NoSuchBeanDefinitionException;
import com.fzdkx.spring.beans.factory.*;
import com.fzdkx.spring.beans.factory.config.*;
import com.fzdkx.spring.core.convert.ConversionService;
import com.fzdkx.spring.util.BeanUtils;
import com.fzdkx.spring.util.StringUtils;

import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 * 可以实例化Bean的抽象Bean工厂
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    // 实例化创建策略类
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String name, BeanDefinition beanDefinition) throws BeansException {
        return doCreateBean(name, beanDefinition);
    }

    private Object doCreateBean(String name, BeanDefinition beanDefinition) {
        Object bean;
        // 记录该Bean正在加载
        beforeSingletonCreation(name);
        // 创建实例
        bean = createBeanInstance(name, beanDefinition);
        // 如果是单例bean，并且正在创建中
        if (beanDefinition.isSingleton() && isSingletonCurrentlyInCreation(name)) {
            // 将Bean封装成FactoryBean，加入三级缓存
            addSingletonFactory(name, () -> getEarlyBeanReference(name, beanDefinition, bean));
        }
        // 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
        applyBeanPostProcessorsBeforeApplyingPropertyValues(name, bean, beanDefinition);
        // 属性赋值
        Object exposedObject = bean;
        populateBean(name, bean, beanDefinition);
        // 初始化Bean - 相关接口回调 ，可能会创建代理bean，并返回
        exposedObject = initializeBean(name, exposedObject, beanDefinition);
        // 调用getSingleton()，从三级缓存中获取 ObjectFactory，调用getObject
        Object earlySingletonReference = getSingleton(name);
        // 如果相等，代表 after方法 未进行代理，那么替换为 getSingleton 返回的对象，因为可能在这尝试代理
        // 如果不相等，那么生成了代理，无需使用 getSingleton 返回的对象
        if (exposedObject == bean) {
            exposedObject = earlySingletonReference;

        }
        // 注册定义了 销毁方法的Bean，当容器关闭时，调用这些Bean的销毁方法
        registerDisposableBeanIfNecessary(name, bean, beanDefinition);
        // 移除正在加载的状态
        afterSingletonCreation(name);
        // 返回最终的对象，如果对象需要代理，那么这个对象一定是代理对象，且只被代理一次
        return exposedObject;
    }

    private void applyBeanPostProcessorsBeforeApplyingPropertyValues(String name, Object bean, BeanDefinition bd) {
        for (BeanPostProcessor bpp : getBeanPostProcessors()) {
            if (bpp instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor ibpp = (InstantiationAwareBeanPostProcessor) bpp;
                ibpp.postProcessPropertyValues(bd, bean, name);
            }
        }

    }

    private void registerDisposableBeanIfNecessary(String name, Object bean, BeanDefinition bd) {
        // 如果这个bean是 非单例，那么不用注册销毁方法
        if (!bd.isSingleton()) {
            return;
        }
        // 如果Bean实现了DisposableBean接口 或 自定义了 destroy-method ，那么进行注册
        if (bean instanceof DisposableBean || !StringUtils.isEmpty(bd.getDestroyMethodName())) {
            registerDisposableBean(name, new DisposableBeanAdapter(bean, name, bd));
        }
    }

    private Object initializeBean(String name, Object bean, BeanDefinition beanDefinition) {
        // 执行Aware接口相关方法
        invokeAwareMethods(name, bean);
        // 2：执行 BeanPostProcess Before方法
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, name);

        // 3：执行Bean的初始化方法
        try {
            invokeInitMethods(name, wrappedBean, beanDefinition);
        } catch (Exception e) {
            throw new RuntimeException("执行Bean的初始化方法失败");
        }
        // 4：执行 BeanPostProcess After方法
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, name);
        return wrappedBean;
    }

    private void invokeAwareMethods(String name, Object bean) {
        if (bean instanceof Aware) {
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
            if (bean instanceof BeanClassLoaderAware) {
                ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
            }
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(name);
            }
        }
    }

    // 执行Bean的初始化相关方法
    private void invokeInitMethods(String name, Object bean, BeanDefinition beanDefinition) throws Exception {
        // 1：判断是否实现 InitializingBean 接口，如果实现，调用其方法
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }
        // 2：判断是否自定义 init-method
        String initMethodName = beanDefinition.getInitMethodName();
        if (!StringUtils.isEmpty(initMethodName)) {
            Method initMethod;
            try {
                // 通过反射获取方法
                initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException(initMethodName + "方法未定义");
            }
            // 调用
            initMethod.invoke(bean);
        }
    }

    private void populateBean(String beanName, Object bean, BeanDefinition bd) {
        if (!bd.hasPropertyValues()) {
            return;
        }
        // Bean需要进行属性填充
        PropertyValues propertyValues = bd.getPropertyValues();
        for (PropertyValue pv : propertyValues.getPropertyValues()) {
            String fieldName = pv.getPropertyName();
            Object value = pv.getValue();
            if (value instanceof BeanReference) {
                BeanReference br = (BeanReference) value;
                value = getBean(br.getBeanName());
                // 属性填充，反射
                BeanUtils.setFieldValue(bean, fieldName, value);
            } else {
                try {
                    ConversionService conversionService = getConversionService();
                    Field field = bean.getClass().getDeclaredField(fieldName);
                    Object convertValue = value;
                    // 如果含有转换器，转换
                    if (conversionService.canConvert(String.class, field.getType())) {
                        convertValue = conversionService.convert(value, field.getType());
                    }
                    // 属性填充，反射
                    BeanUtils.setFieldValue(bean, fieldName, convertValue);
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private Object createBeanInstance(String name, BeanDefinition bd) {
        // 如果是
        Object bean;
        // @Bean方法创建
        if (bd.isMethodCreate()) {
            bean = createBeanInstanceByMethod(name, bd);
        }
        // 构造方法创建
        else {
            bean = createBeanInstanceByConstructor(name, bd);
        }
        return bean;
    }

    private Object createBeanInstanceByMethod(String name, BeanDefinition bd) {
        Method method = bd.getCreateMethod();
        if (method == null) {
            throw new BeansException("bean创建错误");
        }
        // 获取所有的属性
        Class<?>[] parameterTypes = method.getParameterTypes();
        int n = parameterTypes.length;
        Object[] args = new Object[n];
        for (int i = 0; i < n; i++) {
            Class<?> type = parameterTypes[i];
            List<BeanDefinition> bds = getBeanDefinitionByType(type);
            // 容器中没有该Bean的定义
            if (BeanUtils.isEmpty(bds)) {
                throw new NoSuchBeanDefinitionException("找不到bean：" + type);
            }
            // 只有一个，那么就使用这个
            else if (bds.size() == 1) {
                args[i] = getBean(bds.get(0).getName());
            }
            // 有多个，根据参数名称选择
            else {
                // 获取参数名
                String paramName = type.getName();
                args[i] = getBean(paramName);
            }
        }
        // 如果全部获取成功，进行方法调用
        Object configuration = getBean(bd.getConfigurationName());
        try {
            return method.invoke(configuration, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object createBeanInstanceByConstructor(String name, BeanDefinition bd) {

        Constructor<?> constructor = null;
        Class<?> beanClass = bd.getBeanClass();
        // 获取所有构造方法
        Constructor<?>[] constructors = beanClass.getDeclaredConstructors();
        Object[] args = null;
        // 只有一个构造方法，且不是无参构造
        if (constructors.length == 1) {
            Parameter[] parameters = constructors[0].getParameters();
            if (parameters != null && parameters.length != 0) {
                // 获取这个构造方法，进行创建
                constructor = constructors[0];
                // 获取方法参数
                args = getConstructorArgument(bd, constructor);
                if (args == null) {
                    throw new BeanInstanceException("没有对应构造函数");
                }
            }
        } else if (bd.hasConstructorArgumentValues()) {  // 有多个构造方法，并且指定了构造方法
            for (Constructor<?> c : constructors) {
                if ((args = getConstructorArgument(bd, c)) != null) {
                    constructor = c;
                    break;
                }
            }
            if (args == null) {
                throw new BeanInstanceException("没有对应构造函数");
            }
        }
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof BeanReference) {
                    args[i] = getBean(((BeanReference) args[i]).getBeanName());
                }
            }
        }
        return getInstantiationStrategy().instantiate(bd, name, constructor, args);
    }


    protected Object getEarlyBeanReference(String beanName, BeanDefinition bd, Object bean) {
        Object exposedObject = bean;
        if (isHasInstantiationAwareBeanPostProcessors()) {
            for (BeanPostProcessor bpp : getBeanPostProcessors()) {
                if (bpp instanceof InstantiationAwareBeanPostProcessor) {
                    InstantiationAwareBeanPostProcessor ibpp = (InstantiationAwareBeanPostProcessor) bpp;
                    exposedObject = ibpp.getEarlyBeanReference(exposedObject, beanName, bd);
                }
            }
        }
        return exposedObject;
    }

    private Object[] getConstructorArgument(BeanDefinition bd, Constructor<?> constructor) {
        Class<?>[] types = constructor.getParameterTypes();
        ConstructorArgument argument = bd.getConstructorArgument();
        if (argument == null) {
            throw new BeansException("找不到指定构造函数");
        }
        Map<Integer, ConstructorArgument.ValueHolder> map = argument.getIndexedArgumentValues();
        if (types.length != map.size()) {
            return null;
        }
        // 参数个数相等
        int n = types.length;
        Object[] args = new Object[n];
        for (int i = 0; i < n; i++) {
            // 如果map中有，从map中获取
            ConstructorArgument.ValueHolder holder = map.get(i);
            // 如果是引用Bean
            if (holder.getValue() instanceof BeanReference) {
                if ((holder.getType() == types[i])) {
                    args[i] = holder.getValue();
                }
                // 如果没有，代表匹配不上
                else {
                    return null;
                }
            }
            // 如果是直接赋值
            else {

                ConversionService conversionService = getConversionService();
                Object convertValue;
                // 如果含有转换器，转换
                if (conversionService.canConvert(String.class, types[i])) {
                    convertValue = conversionService.convert(holder.getValue(), types[i]);
                    holder.setValue(convertValue);
                }

//                // 获取
//                for (Class<?> aClass : SimpleDataType.getSimpleDataType()) {
//                    if (aClass == types[i]) {
//                        holder.setType(aClass);
//                        holder.setValue(SimpleDataType.parseNumber((String) holder.getValue(), types[i]));
//                        break;
//                    }
//                }
                args[i] = holder.getValue();
            }
        }
        return args;
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }
}
