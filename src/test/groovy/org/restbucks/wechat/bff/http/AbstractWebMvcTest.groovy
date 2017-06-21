package org.restbucks.wechat.bff.http

import me.chanjar.weixin.mp.api.WxMpMessageRouter
import me.chanjar.weixin.mp.api.WxMpService
import org.junit.runner.RunWith
import org.restbucks.wechat.bff.AppRuntime
import org.restbucks.wechat.bff.wechat.WeChatConfig
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

@RunWith(SpringRunner)
@WebMvcTest(controllers = [
        HttpMvcConfig,
        HttpSecurityConfig,
        HttpSessionConfig,
        WeChatConfig]
)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
abstract class AbstractWebMvcTest {

    @Autowired
    protected MockMvc mockMvc

    @MockBean
    protected WeChatUserStore weChatUserStore

    @MockBean
    protected WxMpService wxMpService

    @SpyBean
    protected AppRuntime appRuntime

    @MockBean
    protected WxMpMessageRouter wxMpMessageRouter
}
