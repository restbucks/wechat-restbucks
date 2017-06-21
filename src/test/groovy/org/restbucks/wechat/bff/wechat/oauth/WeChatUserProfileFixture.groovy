package org.restbucks.wechat.bff.wechat.oauth

import me.chanjar.weixin.mp.bean.result.WxMpUser

class WeChatUserProfileFixture {
    private WeChatUserProfile target = new WeChatUserProfile()
    private WxMpUser user = new WxMpUser()

    WeChatUserProfileFixture() {
        target.setOpenId(OpenId.valueOf(UUID.randomUUID().toString()))
        target.setNickname("John Doe")
        target.setAvatar("https://avatar.com/johndoe")

        user.setOpenId(OpenId.valueOf(UUID.randomUUID().toString()).value)
        user.setNickname("John Doe")
        user.setHeadImgUrl("https://avatar.com/johndoe")
    }

    WeChatUserProfileFixture with(OpenId openId) {
        target.setOpenId(openId)
        user.setOpenId(openId.value)
        this
    }

    WeChatUserProfile build() {
        target
    }

    WxMpUser buildUser() {
        user
    }
}
