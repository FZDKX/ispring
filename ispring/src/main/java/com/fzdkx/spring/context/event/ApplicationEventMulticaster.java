package com.fzdkx.spring.context.event;

import com.fzdkx.spring.context.ApplicationEvent;
import com.fzdkx.spring.context.ApplicationListener;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * 事件广播器：添加监听 和 删除监听的方法 以及一个 广播事件的方法
 */
public interface ApplicationEventMulticaster {

    // 添加事件监听
    void addApplicationListener(ApplicationListener<?> listener);

    // 移除事件监听
    void removeApplicationListener(ApplicationListener<?> listener);

    // 广播事件
    void multicastEvent(ApplicationEvent event);
}
