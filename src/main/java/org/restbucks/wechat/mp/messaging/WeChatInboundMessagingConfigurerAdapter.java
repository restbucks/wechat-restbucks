package org.restbucks.wechat.mp.messaging;

import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.context.annotation.Bean;

public abstract class WeChatInboundMessagingConfigurerAdapter {

    @Bean
    protected WxMpMessageRouter wxMpMessageRouter(WxMpService wxMpService) {
        WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);
        configure(router);
        return router;
    }

    protected void configure(WxMpMessageRouter router) {
    }
}
