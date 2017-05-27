package org.restbucks.wechat.bff.wechat.oauth

class WeChatUserOauthAccessTokenFixture {

    private WeChatUserOauthAccessToken target

    WeChatUserOauthAccessTokenFixture() {
        target = new WeChatUserOauthAccessToken(OpenId.valueOf(UUID.randomUUID().toString()),
                "accessToken",
                "refreshToken",
                3600,
                "snsapi_base")
    }

    WeChatUserOauthAccessToken build() {
        return target
    }
}
