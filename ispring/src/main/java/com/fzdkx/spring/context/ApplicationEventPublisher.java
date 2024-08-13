package com.fzdkx.spring.context;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * 事件发布者接口
 */
public interface ApplicationEventPublisher {

    // 发布事件
    void publishEvent(ApplicationEvent event);
}
