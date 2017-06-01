package org.restbucks.wechat.bff.http

import org.junit.runner.RunWith
import org.restbucks.wechat.bff.AppRuntime
import org.restbucks.wechat.bff.http.assembler.WeChatUserProfileResourceAssembler
import org.restbucks.wechat.bff.http.security.CsrfTokenGenerator
import org.restbucks.wechat.bff.http.security.JwtAuthenticationProvider
import org.restbucks.wechat.bff.http.security.JwtIssuer
import org.restbucks.wechat.bff.http.security.RestAuthenticationEntryPoint
import org.restbucks.wechat.bff.wechat.WeChatRuntime
import org.restbucks.wechat.bff.wechat.messaging.WeChatMessageDispatcher
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

@RunWith(SpringRunner)
@WebMvcTest([IndexRestController,
        WeChatWebhookEndpoint,
        WeChatOauthProxy,
        WeChatUserRestController,
        WeChatUserProfileResourceAssembler,
        HttpSecurityConfig,
        RestAuthenticationEntryPoint,
        JwtAuthenticationProvider])
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
abstract class AbstractWebMvcTest {

    @Autowired
    protected MockMvc mockMvc

    @MockBean
    protected WeChatMessageDispatcher weChatMessageDispatcher

    @MockBean
    protected JwtIssuer jwtIssuer

    @SpyBean
    protected WeChatRuntime weChatRuntime

    @SpyBean
    protected AppRuntime appRuntime

    @MockBean
    protected WeChatUserStore weChatUserStore

    @MockBean
    protected CsrfTokenGenerator csrfTokenGenerator

}
