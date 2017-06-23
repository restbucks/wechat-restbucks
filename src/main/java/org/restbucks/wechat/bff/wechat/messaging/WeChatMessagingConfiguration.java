package org.restbucks.wechat.bff.wechat.messaging;

import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import org.restbucks.wechat.mp.messaging.WeChatInboundMessagingConfigurerAdapter;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WeChatMessagingConfiguration extends WeChatInboundMessagingConfigurerAdapter {

    private final QrCodeScannedEventHandler qrCodeScannedEventHandler;
    private final NewSubscribedEventHandler newSubscribedEventHandler;

    @Override
    protected void configure(WxMpMessageRouter router) {
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
