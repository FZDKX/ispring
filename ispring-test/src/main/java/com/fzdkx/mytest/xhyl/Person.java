package com.fzdkx.mytest.xhyl;

import com.fzdkx.spring.context.annotation.Autowired;
import com.fzdkx.spring.context.annotation.Component;
import com.fzdkx.spring.context.annotation.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 */
@Slf4j
@Component
public class Person {

    private Integer id;

    @Autowired
    private Cat cat;

    public Person() {

    }

    public Person(Integer id, Cat cat) {
        this.id = id;
        this.cat = cat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public void sayHello() {
        log.debug("person -- 自定义的初始化方法");
    }

    public void sayBye(){
        log.debug("person -- 自定义的销毁方法");
    }
}
