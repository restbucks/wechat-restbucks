package org.restbucks.wechat.bff.wechat.oauth

class WeChatUserProfileFixture {
    private WeChatUserProfile target = new WeChatUserProfile()

    WeChatUserProfileFixture() {
        target.setOpenId(OpenId.valueOf(UUID.randomUUID().toString()))
        target.setNickname("John Doe")
        target.setAvatar("https://avatar.com/johndoe")
    }

    WeChatUserProfileFixture with(OpenId openId) {
        target.setOpenId(openId)
        this
    }

    WeChatUserProfile build() {
        target
    }
}
