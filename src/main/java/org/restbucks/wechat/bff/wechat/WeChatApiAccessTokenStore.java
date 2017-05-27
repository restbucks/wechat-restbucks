package org.restbucks.wechat.bff.wechat;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeChatApiAccessTokenStore {

    private WeChatRuntime weChatRuntime;

    private RestTemplate restTemplate;

    public WeChatApiAccessTokenStore(WeChatRuntime weChatRuntime,
        @Qualifier("wechat.RestTemplate") RestTemplate restTemplate) {
        this.weChatRuntime = weChatRuntime;
        this.restTemplate = restTemplate;
    }

    public WeChatApiAccessToken get() {
        return restTemplate.getForObject("/cgi-bin/token?"
                + "grant_type=client_credential&appid={appId}&secret={appSecret}",
            WeChatApiAccessToken.class, weChatRuntime.getAppId(), weChatRuntime.getAppSecret());
    }
}
