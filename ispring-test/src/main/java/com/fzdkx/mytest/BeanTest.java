package com.fzdkx.mytest;

import com.fzdkx.mytest.loop.Person;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 */
public class BeanTest {
    Person person;

    public BeanTest(Person person) {
        this.person = person;
        System.out.println("BeanTest 正在创建");
    }

    public void sayHello() {
        System.out.println("hello!!!!");
    }

    public void sayBye() {
        System.out.println("ByeBye!!!!");
    }
}
