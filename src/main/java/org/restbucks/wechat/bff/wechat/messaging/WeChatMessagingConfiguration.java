package org.restbucks.wechat.bff.wechat.messaging;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeChatMessagingConfiguration {

    @Bean
    public WxMpMessageRouter wxMpMessageRouter(
        QrCodeScannedEventHandler qrCodeScannedEventHandler,
        WxMpService wxMpService) {
        // @formatter:off
        WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);
        router
            .rule()
                .msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_SCAN)
                .handler(qrCodeScannedEventHandler)
            .end();
        // @formatter:on
        return router;
    }
}
