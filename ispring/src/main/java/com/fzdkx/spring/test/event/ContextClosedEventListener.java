package com.fzdkx.spring.test.event;

import com.fzdkx.spring.context.ApplicationListener;
import com.fzdkx.spring.context.event.ContextClosedEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 发着呆看星
 * @create 2024/8/13
 */
@Slf4j
public class ContextClosedEventListener  implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.debug("订阅到了 --- 容器关闭，事件源 {}", event);
    }
}
