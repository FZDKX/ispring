package com.fzdkx.spring.context;

import java.util.EventObject;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * 抽象事件类：继承EventObject
 * 后续所有有关事件的类，都有继承它
 */
public abstract class ApplicationEvent extends EventObject {

    // source：触发事件的源对象
    public ApplicationEvent(Object source) {
        super(source);
    }
}
