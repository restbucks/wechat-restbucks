package org.restbucks.wechat.bff.wechat.oauth;

import lombok.Value;

@Value
public class WeChatUserOauthAccessToken {

    private OpenId openId;
    private String accessToken;
    private String refreshToken;
    private int expiresInSeconds;
    private String scope;

    public WeChatUserOauthAccessToken(OpenId openId, String accessToken, String refreshToken,
        int expiresInSeconds, String scope) {
        this.openId = openId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresInSeconds = expiresInSeconds;
        this.scope = scope;
    }
}
