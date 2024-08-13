package com.fzdkx.spring.test;

import com.fzdkx.spring.context.event.ApplicationContextEvent;
import lombok.Data;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * 自定义事件
 */
public class CustomEvent extends ApplicationContextEvent {
    // id
    private Long id;

    // 消息
    private String message;

    public CustomEvent(Object source, Long id, String message) {
        super(source);
        this.id = id;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
