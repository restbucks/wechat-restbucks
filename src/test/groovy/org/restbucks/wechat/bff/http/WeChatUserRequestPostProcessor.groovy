package org.restbucks.wechat.bff.http

import org.restbucks.wechat.bff.http.security.WeChatUserAdapter
import org.restbucks.wechat.bff.wechat.oauth.OpenId
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.web.servlet.request.RequestPostProcessor

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication

class WeChatUserRequestPostProcessor implements RequestPostProcessor {

    private OpenId openId = OpenId.valueOf(UUID.randomUUID().toString())
    private String csrfToken = "csrfToken"


    WeChatUserRequestPostProcessor with(OpenId openId) {
        this.openId = openId
        this
    }

    WeChatUserRequestPostProcessor csrfToken(String csrfToken) {
        this.csrfToken = csrfToken
        this
    }

    @Override
    MockHttpServletRequest postProcessRequest(
            MockHttpServletRequest mockHttpServletRequest) {

        RequestPostProcessor delegate = authentication(new WeChatUserAdapter(openId))

        mockHttpServletRequest.addHeader("x-csrf-token", csrfToken)

        delegate.postProcessRequest(mockHttpServletRequest)
    }

    static weChatUser() {
        new WeChatUserRequestPostProcessor()
    }

}
