package org.restbucks.wechat.bff.wechat.messaging

import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.restbucks.wechat.bff.wechat.*
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserProfile
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserProfileFixture

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import static org.mockito.BDDMockito.given
import static org.mockito.MockitoAnnotations.initMocks

class WeChatMessageSenderTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options().dynamicPort())

    WeChatMessageSender subject

    WeChatRuntime weChatRuntime

    WeChatConfig weChatConfig

    @Mock
    WeChatApiAccessTokenStore apiAccessTokenStore

    @Before
    void setup() {
        initMocks(this)
        weChatRuntime = new WeChatRuntime(apiBaseUri: "http://localhost:${wireMockRule.port()}")
        weChatConfig = new WeChatConfig(weChatRuntime)
        subject = new WeChatMessageSender(weChatConfig.restTemplate(),
                weChatRuntime, apiAccessTokenStore)
    }

    @Test
    void "it should send news message to wechat"() {

        WeChatApiAccessToken apiToken = new WeChatApiAccessTokenFixture().build()
        WeChatUserProfile userProfile = new WeChatUserProfileFixture().build()
        NewsMessage message = new NewsMessage(userProfile.openId.value,
                NewsMessage.Article.builder().title("title")
                        .description("desc").image("img").url("uri"))

        given(apiAccessTokenStore.get()).willReturn(apiToken)

        givenThat(post(urlEqualTo("/cgi-bin/message/custom/send?" +
                "access_token=${apiToken.accessToken}"))
                .withRequestBody(equalToJson("""
                    {
                       "touser":"${userProfile.openId.value}",
                       "msgtype":"news",
                       "news":{
                           "articles": [{
                                "title":"title",
                                "description":"desc",
                                "url":"uri",
                                "picurl":"img"
                            }]
                       }
                    }
                """))
                .willReturn(aResponse()))

        subject.send(message)
    }
}
