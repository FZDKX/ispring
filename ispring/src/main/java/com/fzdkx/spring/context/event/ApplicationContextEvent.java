package com.fzdkx.spring.context.event;

import com.fzdkx.spring.context.ApplicationContext;
import com.fzdkx.spring.context.ApplicationEvent;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * 定义事件的抽象类，所有的事件都要继承这个类
 */
public class ApplicationContextEvent extends ApplicationEvent {

    public ApplicationContextEvent(Object source) {
        super(source);
    }

    // 获取触发事件的源对象
    public final ApplicationContext getApplicationContext() {
        // 返回发生事件的对象
        return (ApplicationContext) getSource();
    }

}
