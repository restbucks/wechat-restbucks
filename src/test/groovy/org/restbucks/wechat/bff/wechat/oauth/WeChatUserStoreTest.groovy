package org.restbucks.wechat.bff.wechat.oauth

import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.restbucks.wechat.bff.wechat.WeChatApiAccessToken
import org.restbucks.wechat.bff.wechat.WeChatApiAccessTokenFixture
import org.restbucks.wechat.bff.wechat.WeChatApiAccessTokenStore
import org.restbucks.wechat.bff.wechat.WeChatConfig
import org.restbucks.wechat.bff.wechat.WeChatRuntime

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import static org.mockito.BDDMockito.given
import static org.mockito.MockitoAnnotations.initMocks

class WeChatUserStoreTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options().dynamicPort())

    WeChatUserStore subject

    WeChatRuntime weChatRuntime

    def weChatConfig

    @Mock
    WeChatApiAccessTokenStore apiAccessTokenStore

    @Before
    void setup() {
        initMocks(this)
        weChatRuntime = new WeChatRuntime(apiBaseUri: "http://localhost:${wireMockRule.port()}")
        weChatConfig = new WeChatConfig()
        subject = new WeChatUserStore(weChatConfig.restTemplate(weChatRuntime),
                weChatRuntime, apiAccessTokenStore)
    }

    @Test
    void "it should fetch user access token with oauth code"() {
        String code = "code"

        givenThat(get(urlEqualTo("/sns/oauth2/access_token"
                + "?appid=${weChatRuntime.appId}&secret=${weChatRuntime.appSecret}"
                + "&code=${code}&grant_type=authorization_code"))
                .willReturn(aResponse()
                .withHeader("Content-Type", "text/plain")
                .withBody("""
                    {
                       "access_token":"ACCESS_TOKEN",
                       "expires_in":7200,
                       "refresh_token":"REFRESH_TOKEN",
                       "openid":"OPENID",
                       "scope":"snsapi_base"
                    }
                """)))

        def actual = subject.exchangeAccessTokenWith(code)

        assert "ACCESS_TOKEN" == actual.accessToken
        assert 7200 == actual.expiresInSeconds
        assert "REFRESH_TOKEN" == actual.refreshToken
        assert "OPENID" == actual.openId.value
        assert "snsapi_base" == actual.scope
    }

    @Test
    void "it should fetch user profile with open id"() {

        WeChatApiAccessToken apiToken = new WeChatApiAccessTokenFixture().build()
        WeChatUserProfile profile = new WeChatUserProfileFixture().with(OpenId.valueOf("OPENID")).build()

        given(apiAccessTokenStore.get()).willReturn(apiToken)

        givenThat(get(urlEqualTo("/cgi-bin/user/info?" +
                "access_token=${apiToken.accessToken}&openid=${profile.openId.value}&lang=zh_CN"))
                .willReturn(aResponse()
                .withHeader("Content-Type", "text/plain")
                .withBody("""
                    {
                        "subscribe": 1, 
                        "openid": "${profile.openId.value}", 
                        "nickname": "Band", 
                        "sex": 1, 
                        "language": "zh_CN", 
                        "city": "Guangzhou", 
                        "province": "Guangdong", 
                        "country": "China", 
                        "headimgurl":    "this is avatar", 
                       "subscribe_time": 1382694957
                    }
                """)))

        def actual = subject.findUserProfile(profile.openId)

        assert profile.openId == actual.openId
        assert "Band" == actual.nickname
        assert "this is avatar" == actual.avatar
    }
}
