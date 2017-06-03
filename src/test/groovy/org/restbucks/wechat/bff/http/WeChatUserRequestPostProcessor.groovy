package org.restbucks.wechat.bff.http

import org.restbucks.wechat.bff.http.security.JwtIssuer
import org.restbucks.wechat.bff.wechat.oauth.OpenId
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.web.servlet.request.RequestPostProcessor

import javax.servlet.http.Cookie

class WeChatUserRequestPostProcessor implements RequestPostProcessor {

    private OpenId openId = OpenId.valueOf(UUID.randomUUID().toString())
    private String csrfToken = "csrfToken"
    private JwtIssuer jwtIssuer

    WeChatUserRequestPostProcessor(JwtIssuer jwtIssuer) {
        this.jwtIssuer = jwtIssuer
    }

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
        mockHttpServletRequest.setCookies(new Cookie("wechat.restbucks.org.user",
                jwtIssuer.buildUserJwt(openId, csrfToken)))
        mockHttpServletRequest.addHeader("x-csrf-token", csrfToken)
        return mockHttpServletRequest
    }

}
