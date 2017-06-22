package org.restbucks.wechat.bff.http

import me.chanjar.weixin.mp.api.WxMpMessageRouter
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.api.WxMpUserService
import org.junit.runner.RunWith
import org.restbucks.wechat.bff.AppRuntime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

@RunWith(SpringRunner)
@WebMvcTest(controllers = [
        HttpMvcConfig,
        HttpSecurityConfig,
        HttpSessionConfig]
)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@TestPropertySource(properties = ["session.mode=none"])
//see https://stackoverflow.com/questions/44692666/spring-session-is-not-compatible-with-mockhttpsession
abstract class AbstractWebMvcTest {

    @Autowired
    protected MockMvc mockMvc

    @MockBean
    protected WxMpService wxMpService

    @MockBean
    protected WxMpUserService wxMpUserService

    @SpyBean
    protected AppRuntime appRuntime

    @MockBean
    protected WxMpMessageRouter wxMpMessageRouter
}
