package org.restbucks.wechat.bff.wechat.messaging;

import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WeChatMessagingConfiguration {

    private final QrCodeScannedEventHandler qrCodeScannedEventHandler;
    private final NewSubscribedEventHandler newSubscribedEventHandler;

    @Bean
    public WxMpMessageRouter wxMpMessageRouter(
        WxMpService wxMpService) {
        WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);
        configure(router);

        return router;
    }

    private void configure(WxMpMessageRouter router) {
        // @formatter:off
        router
            .rule()
                .msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_SCAN).event(WxConsts.EVT_SUBSCRIBE)
                .handler(qrCodeScannedEventHandler)
            .end();
        // @formatter:on
    }
}
