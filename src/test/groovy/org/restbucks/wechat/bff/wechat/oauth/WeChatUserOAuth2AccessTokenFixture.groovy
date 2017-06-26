package org.restbucks.wechat.bff.wechat.oauth

import com.github.hippoom.wechat.oauth.OpenId
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken

class WeChatUserOAuth2AccessTokenFixture {

    private WxMpOAuth2AccessToken target

    WeChatUserOAuth2AccessTokenFixture() {
        target = new WxMpOAuth2AccessToken()
        target.setOpenId(OpenId.valueOf(UUID.randomUUID().toString()).value)
        target.setAccessToken("accessToken")
        target.setRefreshToken("refreshToken")
        target.setExpiresIn(3600)
        target.setScope("snsapi_base")
    }

    WeChatUserOAuth2AccessTokenFixture with(OpenId openId) {
        target.setOpenId(openId.value)
        this
    }

    WxMpOAuth2AccessToken buildToken() {
        return target
    }
}
