package com.fzdkx.mytest.loop;

import com.fzdkx.spring.beans.factory.DisposableBean;
import com.fzdkx.spring.beans.factory.InitializingBean;
import com.fzdkx.spring.context.annotation.Autowired;
import com.fzdkx.spring.context.annotation.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 */
@Slf4j
@Component
public class Cat implements InitializingBean , DisposableBean {
    private Integer id;

    @Autowired
    Person person;

    public Cat() {
    }

    public Cat(Integer id, Person person) {
        this.id = id;
        this.person = person;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public void destroy() throws Exception {
        log.debug("cat -- 接口的销毁方法");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("cat -- 接口的初始化方法");
    }
}
