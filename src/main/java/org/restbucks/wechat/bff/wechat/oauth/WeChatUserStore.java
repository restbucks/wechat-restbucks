package org.restbucks.wechat.bff.wechat.oauth;

import lombok.extern.slf4j.Slf4j;
import org.restbucks.wechat.bff.wechat.WeChatApiAccessTokenStore;
import org.restbucks.wechat.bff.wechat.WeChatRuntime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class WeChatUserStore {

    private final RestTemplate restTemplate;

    private final WeChatRuntime weChatRuntime;

    private final WeChatApiAccessTokenStore apiAccessTokenStore;

    public WeChatUserStore(@Qualifier("wechat.RestTemplate") RestTemplate restTemplate,
        WeChatRuntime weChatRuntime,
        WeChatApiAccessTokenStore apiAccessTokenStore) {
        this.restTemplate = restTemplate;
        this.weChatRuntime = weChatRuntime;
        this.apiAccessTokenStore = apiAccessTokenStore;
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

    public WeChatUserProfile findUserProfile(OpenId openId) {
        return restTemplate
            .getForObject(
                "/cgi-bin/user/info?access_token={token}&openid={openId}&lang=zh_CN",
                WeChatUserProfile.class,
                apiAccessTokenStore.get().getAccessToken(),
                openId.getValue());
    }
}
