package com.fzdkx.mytest.event;

import com.fzdkx.spring.context.ApplicationListener;
import com.fzdkx.spring.context.annotation.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 * 自定义事件 监听器
 */
@Slf4j
@Component
public class CustomEventListener implements ApplicationListener<CustomEvent> {
    @Override
    public void onApplicationEvent(CustomEvent event) {
        log.debug("接受到自定义事件：id -- {} , 事件源 -- {} . 消息 -- {}", event.getId(), event.getSource(), event.getMessage());
    }
}
