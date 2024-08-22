package com.fzdkx.mytest;

import com.fzdkx.mytest.loop.Person;
import com.fzdkx.spring.context.annotation.Bean;
import com.fzdkx.spring.context.annotation.ComponentScan;
import com.fzdkx.spring.context.annotation.Configuration;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 */
@Configuration
@ComponentScan("com.fzdkx.mytest")
public class MyConfiguration {
    @Bean(initMethod = "sayHello" , destroyMethod = "sayBye")
    public BeanTest beanTest(Person person) {
        System.out.println("person ==" + person);
        return new BeanTest(person);
    }
}
