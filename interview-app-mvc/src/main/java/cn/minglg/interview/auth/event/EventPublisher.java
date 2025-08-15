package cn.minglg.interview.auth.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * ClassName:EventPublisher
 * Package:cn.minglg.interview.event
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/6/18
 * @Version 1.0
 */

@Component
public class EventPublisher implements ApplicationEventPublisherAware {
    /**
     * 底层发送事件⽤的组件，SpringBoot会通过ApplicationEventPublisherAware接⼝
     * ⾃动注⼊给我们
     * 事件是⼴播出去的。所有监听这个事件的监听器都可以收到
     */
    ApplicationEventPublisher eventPublisher;

    /**
     * 所有事件都可以发
     *
     * @param event
     */
    public void sendEvent(ApplicationEvent event) {
        //调⽤底层API发送事件
        eventPublisher.publishEvent(event);
    }

    /**
     * 会被⾃动调⽤，把真正发事件的底层组组件给我们注⼊进来
     *
     * @param eventPublisher event publisher to be used by this object
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
