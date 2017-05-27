package org.restbucks.wechat.bff.wechat

class WeChatApiAccessTokenFixture {

    private WeChatApiAccessToken target

    WeChatApiAccessTokenFixture() {
        target = new WeChatApiAccessToken(
                "accessToken",
                3600)
    }

    WeChatApiAccessToken build() {
        return target
    }
}
