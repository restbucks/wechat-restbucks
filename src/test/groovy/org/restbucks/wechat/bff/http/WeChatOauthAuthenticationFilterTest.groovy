package org.restbucks.wechat.bff.http

import org.junit.Test
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserOauthAccessToken
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserOauthAccessTokenFixture

import static org.mockito.BDDMockito.given
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class WeChatOauthAuthenticationFilterTest extends AbstractWebMvcTest {

    @Test
    void it_should_redirect_user_to_origin_uri_when_wechat_oauth_is_finished() throws Exception {

        String code = "codeToExchangeWeChatUserAccessToken"
        String plainUrl = "http://www.example.com/index.html?a=b#/route"
        String state = Base64.getUrlEncoder().encodeToString(plainUrl.getBytes("UTF-8"))
        WeChatUserOauthAccessToken accessToken = new WeChatUserOauthAccessTokenFixture().build()

        given(weChatUserStore.exchangeAccessTokenWith(code))
                .willReturn(accessToken)

        this.mockMvc.perform(get("/webhooks/wechat/oauth")
                .param("state", state) // it seems that the controller will decode the parameter automatically only for browser request
                .param("code", code))
                .andDo(print())
                .andExpect(authenticated().withAuthenticationPrincipal(accessToken.openId))
                .andExpect(cookie().exists("wechat.restbucks.org.csrfToken"))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(plainUrl))
    }

}