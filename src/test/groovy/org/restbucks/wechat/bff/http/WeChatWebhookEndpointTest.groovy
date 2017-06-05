package org.restbucks.wechat.bff.http

import org.junit.Test
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserOauthAccessToken
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserOauthAccessTokenFixture

import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.not
import static org.mockito.BDDMockito.given
import static org.mockito.Mockito.verify
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class WeChatWebhookEndpointTest extends AbstractWebMvcTest {

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

        verify(weChatMessageDispatcher).dispatch(payload)
    }


    @Test
    void it_should_redirect_user_to_origin_uri_when_wechat_oauth_is_finished() throws Exception {

        String code = "codeToExchangeWeChatUserAccessToken"
        String state = "http://www.example.com/index.html?a=b#/route"
        WeChatUserOauthAccessToken accessToken = new WeChatUserOauthAccessTokenFixture().build()

        given(weChatUserStore.exchangeAccessTokenWith(code))
                .willReturn(accessToken)

        this.mockMvc.perform(get("/webhooks/wechat/oauth")
                .param("state", state) // it seems that the controller will decode the parameter automatically only for browser request
                .param("code", code))
                .andDo(print())
                .andExpect(authenticated().withAuthenticationPrincipal(accessToken.openId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(state))
    }
}
