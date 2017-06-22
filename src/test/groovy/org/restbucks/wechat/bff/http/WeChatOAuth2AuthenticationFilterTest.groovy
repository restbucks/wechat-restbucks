package org.restbucks.wechat.bff.http

import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken
import org.junit.Test
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserOAuth2AccessTokenFixture

import static org.mockito.BDDMockito.given
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class WeChatOAuth2AuthenticationFilterTest extends AbstractWebMvcTest {


    @Test
    void it_should_redirect_user_to_origin_uri_when_wechat_oauth_is_finished() throws Exception {

        String code = "codeToExchangeWeChatUserAccessToken"
        String plainUrl = "http://www.example.com/index.html?a=b#/route"
        String state = Base64.getUrlEncoder().encodeToString(plainUrl.getBytes("UTF-8"))
        WxMpOAuth2AccessToken accessToken = new WeChatUserOAuth2AccessTokenFixture().buildToken()

        given(wxMpService.oauth2getAccessToken(code))
                .willReturn(accessToken)

        this.mockMvc.perform(get("/wechat/oauth/token")
                .param("state", state) // it seems that the controller will decode the parameter automatically only for browser request
                .param("code", code))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(plainUrl))
                .andExpect(authenticated())
                .andExpect(cookie().exists("wechat.restbucks.org.csrfToken"))
    }

}