package org.restbucks.wechat.bff.http

import org.junit.Test
import org.junit.runner.RunWith
import org.restbucks.wechat.bff.AppRuntime
import org.restbucks.wechat.bff.wechat.WeChatRuntime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner)
@WebMvcTest([WeChatOauthProxy, AppRuntime, WeChatRuntime])
class WeChatOauthProxyTest {

    @Autowired
    private MockMvc mvc

    @SpyBean
    private WeChatRuntime weChatRuntime

    @Test
    void it_should_redirect_to_wechat_to_finish_oauth_protocol() throws Exception {

        String origin = "http://www.example.com/index.html?a=b#/route"
        String encodedOrigin = URLEncoder.encode(origin, "UTF-8")
        def redirectUri = "https%3A%2F%2Fwechat.restbucks.org%2Fwebhooks%2Fwechat%2Foauth"

        this.mvc.perform(get("/wechat/oauth/authorize")
                .param("origin", origin)) // it seems that the controller will decode the parameter automatically only for browser request
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(
                """https://open.weixin.qq.com/connect/oauth2/authorize?appid=${weChatRuntime.getAppId()}&redirect_uri=${redirectUri}&response_type=code&scope=snsapi_base&state=${encodedOrigin}#wechat_redirect""",
        ))

    }
}