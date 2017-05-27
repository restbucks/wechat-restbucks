package org.restbucks.wechat.bff.wechat

import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options

class WeChatApiAccessTokenStoreTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options().dynamicPort())

    def subject

    def weChatRuntime

    def weChatConfig

    @Before
    void setup() {
        weChatRuntime = new WeChatRuntime(apiBaseUri: "http://localhost:${wireMockRule.port()}")
        weChatConfig = new WeChatConfig(weChatRuntime)
        subject = new WeChatApiAccessTokenStore(weChatRuntime, weChatConfig.restTemplate())
    }

    @Test
    void "it should fetch access token from wechat"() {


        givenThat(get(urlEqualTo("/cgi-bin/token?grant_type=client_credential" +
                "&appid=${weChatRuntime.appId}&secret=${weChatRuntime.appSecret}"))
                .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "access_token": "ACCESS_TOKEN",
                        "expires_in": 7200
                    }
                """)))

        WeChatApiAccessToken actual = subject.get()

        assert "ACCESS_TOKEN" == actual.accessToken
        assert 7200 == actual.expiresInSeconds
    }
}
