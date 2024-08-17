package com.fzdkx.spring.test;

import com.fzdkx.spring.context.annotation.Bean;
import com.fzdkx.spring.context.annotation.ComponentScan;
import com.fzdkx.spring.context.annotation.Configuration;
import com.fzdkx.spring.test.xhyl.Person;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 */
@Configuration
@ComponentScan("com.fzdkx.spring.test")
public class MyConfiguration {
    @Bean
    public BeanTest beanTest(Person person) {
        return new BeanTest(person);
    }
}
