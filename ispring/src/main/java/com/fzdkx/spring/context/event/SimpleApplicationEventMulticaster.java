package com.fzdkx.spring.context.event;

import com.fzdkx.spring.context.ApplicationEvent;
import com.fzdkx.spring.context.ApplicationListener;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * 简单是事件广播器
 */
public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

    // 广播事件
    @Override
    public void multicastEvent(ApplicationEvent event) {
        // 获取所有有关这个事件的 监听器
        // 调用监听器的方法，处理事件
        for (ApplicationListener listener : getApplicationListeners(event)) {
            listener.onApplicationEvent(event);
        }
    }
}
