package org.restbucks.wechat.bff.wechat.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeChatUserOauthAccessToken {

    @JsonProperty("openid")
    @JsonDeserialize(using = OpenIdDeserializer.class)
    private OpenId openId;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("expires_in")
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
