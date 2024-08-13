package com.fzdkx.spring.beans.factory;

/**
 * @author 发着呆看星
 * @create 2024/8/12
 * 工厂Bean：一种特殊的Bean，可以创建Bean
 *      |-- 在容器启动时，会创建FactoryBean实例，加入 三级缓存中，此时并不会调用 getObject 方法创建 bean
 *      |-- 当我们需要 bean时，我们可以调用getBean获取，此时才会真正的调用 getObject 创建bean
 *          |-- 如果bean是当你的， 这个bean并不会存入三级缓存中，而是会存入 一个全是由FactoryBean创建的bean的map中
 *          |-- 如果我们再次需要，只需从这个map中取
 *      |-- 如果想要获取 FactoryBean 对象，需要在 name前加入 &
 *      |-- FactoryBean创建的Bean不会经历Bean的生命周期
 */
public interface FactoryBean<T> {

    T getObject() throws Exception;

    Class<?> getObjectType();

    boolean isSingleton();
}
