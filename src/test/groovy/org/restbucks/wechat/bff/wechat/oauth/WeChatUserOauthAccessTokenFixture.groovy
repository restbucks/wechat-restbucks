package org.restbucks.wechat.bff.wechat.oauth

import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken

class WeChatUserOauthAccessTokenFixture {

    private WeChatUserOauthAccessToken target
    private WxMpOAuth2AccessToken target2

    WeChatUserOauthAccessTokenFixture() {
        def openId = OpenId.valueOf(UUID.randomUUID().toString())
        def accessToken = "accessToken"
        def refreshToken = "refreshToken"
        def expiresInSeconds = 3600
        def scope = "snsapi_base"
        target = new WeChatUserOauthAccessToken(openId,
                accessToken,
                refreshToken,
                expiresInSeconds,
                scope)

        target2 = new WxMpOAuth2AccessToken()
        target2.setOpenId(openId.value)
        target2.setAccessToken(accessToken)
        target2.setRefreshToken(refreshToken)
        target2.setExpiresIn(expiresInSeconds)
        target2.setScope(scope)
    }

    WeChatUserOauthAccessToken build() {
        return target
    }

    WxMpOAuth2AccessToken buildToken() {
        return target2
    }
}
