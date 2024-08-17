package com.fzdkx.spring.test;

import com.fzdkx.spring.test.xhyl.Person;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 */
public class BeanTest {
    Person person;

    public BeanTest(Person person) {
        System.out.println("BeanTest 正在创建");
    }
}
