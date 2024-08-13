package com.fzdkx.spring.context;

import java.util.EventListener;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * 事件监听器
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    // 处理事件
    void onApplicationEvent(E event);
}
