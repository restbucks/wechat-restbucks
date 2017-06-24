package org.restbucks.wechat.bff.http

import org.restbucks.wechat.mp.autoconfigure.web.WeChatMpWebConfiguration
import org.restbucks.wechat.mp.autoconfigure.web.WeChatMpWebMethodConfiguration
import org.restbucks.wechat.mp.autoconfigure.web.security.WeChatMpWebSecurityConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import([
        WeChatMpWebConfiguration,
        WeChatMpWebSecurityConfiguration,
        WeChatMpWebMethodConfiguration
])
class WeChatMpHttpConfigurationForTest {

}
