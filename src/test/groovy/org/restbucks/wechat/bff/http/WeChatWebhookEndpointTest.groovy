package org.restbucks.wechat.bff.http

import org.junit.Test
import org.junit.runner.RunWith
import org.restbucks.wechat.bff.http.security.CsrfTokenGenerator
import org.restbucks.wechat.bff.http.security.JwtAuthenticationProvider
import org.restbucks.wechat.bff.http.security.JwtIssuer
import org.restbucks.wechat.bff.http.security.RestAuthenticationEntryPoint
import org.restbucks.wechat.bff.time.Clock
import org.restbucks.wechat.bff.wechat.WeChatRuntime
import org.restbucks.wechat.bff.wechat.messaging.WeChatMessageDispatcher
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserOauthAccessToken
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserOauthAccessTokenFixture
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult

import javax.servlet.http.Cookie

import static org.hamcrest.Matchers.*
import static org.junit.Assert.assertThat
import static org.mockito.BDDMockito.given
import static org.mockito.Mockito.verify
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner)
@WebMvcTest([IndexRestController,
        WeChatWebhookEndpoint,
        HttpSecurityConfig,
        RestAuthenticationEntryPoint,
        JwtAuthenticationProvider,
        JwtIssuer,
        Clock])
class WeChatWebhookEndpointTest {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private WeChatRuntime weChatRuntime

    @MockBean
    private WeChatMessageDispatcher messageDispatcher

    @MockBean
    private WeChatUserStore userStore

    @MockBean
    private CsrfTokenGenerator csrfTokenGenerator

    @MockBean
    private JwtIssuer jwtIssuer

    @Test
    void returns_echostr_to_get_authenticated_by_wechat_server() {

        def timestamp = "timestamp"
        def nonce = "nonce"

        given(weChatRuntime.calculateSignatureWith(timestamp, nonce)).willReturn("good")

        // @formatter:off
        this.mockMvc.perform(
                    get("/webhooks/wechat/messaging")
                    .param("signature", "good")
                    .param("echostr", "echostr")
                    .param("timestamp", timestamp)
                    .param("nonce", nonce)
                )
                .andDo(print())
	            .andExpect(status().isOk())
                .andExpect(content().string("echostr"))
        // @formatter:on
    }

    @Test
    void returns_error_other_than_echostr_to_refuse_authentication_by_wechat_server() {

        def timestamp = "timestamp"
        def nonce = "nonce"

        given(weChatRuntime.calculateSignatureWith(timestamp, nonce)).willReturn("good")

        // @formatter:off
        this.mockMvc.perform(
                    get("/webhooks/wechat/messaging")
                    .param("signature", "bad")
                    .param("echostr", "echostr")
                    .param("timestamp", timestamp)
                    .param("nonce", nonce)
                )
                .andDo(print())
	            .andExpect(status().isOk())
                .andExpect(content().string(not(equalTo("echostr"))))
        // @formatter:on
    }

    @Test
    void when_receives_qrcode_scanned_event() {

        String payload = """
            <xml>
                <ToUserName><![CDATA[toUser]]></ToUserName>
                <FromUserName><![CDATA[FromUser]]></FromUserName>
                <CreateTime>123456789</CreateTime>
                <MsgType><![CDATA[event]]></MsgType>
                <Event><![CDATA[subscribe]]></Event>
                <EventKey><![CDATA[qrscene_123123]]></EventKey>
                <Ticket><![CDATA[TICKET]]></Ticket>
            </xml>
        """
        // @formatter:off
        this.mockMvc.perform(
                    post("/webhooks/wechat/messaging")
                    .content(payload)
                )
                .andDo(print())
	            .andExpect(status().isOk())
        // @formatter:on

        verify(messageDispatcher).dispatch(payload)
    }


    @Test
    void it_should_redirect_user_to_origin_uri_when_wechat_oauth_is_finished() throws Exception {

        String code = "codeToExchangeWeChatUserAccessToken"
        String state = "http://www.example.com/index.html?a=b#/route"
        String csrfToken = "csrfToken"
        WeChatUserOauthAccessToken accessToken = new WeChatUserOauthAccessTokenFixture().build()
        String userJwt = "userJwt"

        given(userStore.exchangeAccessTokenWith(code))
                .willReturn(accessToken)
        given(csrfTokenGenerator.generate())
                .willReturn(csrfToken)
        given(jwtIssuer.buildUserJwt(accessToken.openId, csrfToken))
                .willReturn(userJwt)

        MvcResult mvcResult = this.mockMvc.perform(get("/webhooks/wechat/oauth")
                .param("state", state) // it seems that the controller will decode the parameter automatically only for browser request
                .param("code", code))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(state))
                .andReturn()


        Cookie userCookie = mvcResult.getResponse().getCookie("wechat.restbucks.org.user")
        Cookie csrfTokenCookie = mvcResult.getResponse().getCookie("wechat.restbucks.org.csrfToken")

        // verify userCookie
        assertThat(userCookie.getValue(), is(userJwt))
        assertThat(userCookie.isHttpOnly(), is(true))
        assertThat(userCookie.getMaxAge(), is(jwtIssuer.getExpiresInSeconds()))

        // verify csrfTokenCookie
        assertThat(csrfTokenCookie.getValue(), is(csrfToken))
        assertThat(csrfTokenCookie.isHttpOnly(), is(false))
        assertThat(csrfTokenCookie.getMaxAge(), is(jwtIssuer.getExpiresInSeconds()))
    }
}
