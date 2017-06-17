package org.restbucks.wechat.bff.http

import org.junit.Test

import static org.mockito.BDDMockito.given
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class WeChatOauthRedirectorTest extends AbstractWebMvcTest {

    @Test
    void it_should_redirect_to_wechat_to_finish_oauth_protocol() throws Exception {

        String origin = "http://www.example.com/index.html?a=b#/route"
        String encodedOrigin = Base64.urlEncoder.encodeToString(origin.getBytes("UTF-8"))


        def redirectUri = "https://wechat.restbucks.org/webhooks/wechat/oauth"

        given(weChatMpService.oauth2buildAuthorizationUrl(redirectUri,
                "snsapi_base", encodedOrigin)).willReturn("https://open.weixin.qq.com")


        this.mockMvc.perform(get("/wechat/oauth/authorize")
                .param("origin", origin)) // it seems that the controller will decode the parameter automatically only for browser request
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("https://open.weixin.qq.com"))

    }
}