package org.restbucks.wechat.bff.wechat.oauth

import me.chanjar.weixin.mp.bean.result.WxMpUser
import org.restbucks.wechat.oauth.OpenId

class WeChatUserProfileFixture {
    private WxMpUser target = new WxMpUser()

    WeChatUserProfileFixture() {
        target.setOpenId(OpenId.valueOf(UUID.randomUUID().toString()).value)
        target.setNickname("John Doe")
        target.setHeadImgUrl("https://avatar.com/johndoe")
    }

    WeChatUserProfileFixture with(OpenId openId) {
        target.setOpenId(openId.value)
        this
    }

    WxMpUser build() {
        target
    }
}
