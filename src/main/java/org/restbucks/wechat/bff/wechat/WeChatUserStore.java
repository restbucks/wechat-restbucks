package org.restbucks.wechat.bff.wechat;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class WeChatUserStore {

    @NonNull
    private final RestTemplate restTemplate;

    @NonNull
    private final WeChatRuntime weChatRuntime;

    public WeChatUserStore(@Qualifier("wechat.RestTemplate") RestTemplate restTemplate,
        WeChatRuntime weChatRuntime) {
        this.restTemplate = restTemplate;
        this.weChatRuntime = weChatRuntime;
    }

    public WeChatUserOauthAccessToken exchangeAccessTokenWith(String code) {

        return restTemplate
            .getForObject(
                "/sns/oauth2/access_token"
                    + "?appid={appId}&secret={appSecret}"
                    + "&code={code}&grant_type=authorization_code",
                WeChatUserOauthAccessToken.class,
                weChatRuntime.getAppId(),
                weChatRuntime.getAppSecret(),
                code);
    }
}
