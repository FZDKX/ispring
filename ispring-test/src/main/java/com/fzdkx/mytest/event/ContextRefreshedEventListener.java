package com.fzdkx.mytest.event;

import com.fzdkx.spring.context.ApplicationListener;
import com.fzdkx.spring.context.annotation.Component;
import com.fzdkx.spring.context.event.ContextRefreshedEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 */
@Slf4j
@Component
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.debug("订阅到了 --- 容器刷新，事件源 {}", event);
    }
}
