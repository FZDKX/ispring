package com.fzdkx.mytest.loop;

import com.fzdkx.spring.context.annotation.Autowired;
import com.fzdkx.spring.context.annotation.Component;

/**
 * @author 发着呆看星
 * @create 2024/8/22
 */
@Component
public class B {

    private A a;


    public B() {
    }

    @Autowired  // 指定构造方法注入，会选择这个构造方法
    public B(A a) {
        this.a = a;
    }

}
