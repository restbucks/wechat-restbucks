package org.restbucks.wechat.bff.http

import org.junit.Test
import org.junit.runner.RunWith
import org.restbucks.wechat.bff.http.assembler.WeChatUserProfileResourceAssembler
import org.restbucks.wechat.bff.http.security.JwtAuthenticationProvider
import org.restbucks.wechat.bff.http.security.JwtIssuer
import org.restbucks.wechat.bff.http.security.RestAuthenticationEntryPoint
import org.restbucks.wechat.bff.time.Clock
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserOauthAccessTokenFixture
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserProfileFixture
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

import javax.servlet.http.Cookie

import static org.mockito.BDDMockito.given
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.payload.PayloadDocumentation.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner)
@WebMvcTest([WeChatUserRestController,
        WeChatUserProfileResourceAssembler,
        HttpSecurityConfig,
        RestAuthenticationEntryPoint,
        JwtAuthenticationProvider,
        JwtIssuer,
        Clock])
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
class WeChatUserRestControllerTest {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private JwtIssuer jwtIssuer

    @MockBean
    private WeChatUserStore weChatUserStore

    @Test
    void returns_wechat_user_profile() {

        def userJwt = "aUserJwt"
        def csrfToken = "csrfToken"
        def userToken = new WeChatUserOauthAccessTokenFixture().build()
        def userProfile = new WeChatUserProfileFixture().with(userToken.openId).build()

        given(jwtIssuer.verified(userJwt, csrfToken)).willReturn(userToken.openId)

        given(weChatUserStore.findUserProfile(userToken.openId)).willReturn(userProfile)

        // @formatter:off
        this.mockMvc.perform(
                    get("/rel/wechat/user/profile/me")
                    .cookie(new Cookie("wechat.restbucks.org.user", userJwt))
                    .header("x-csrf-token", csrfToken)
                )
                .andDo(print())
	            .andExpect(status().isOk())
                .andExpect(jsonPath("openId").doesNotExist()) // hide internal state
                .andDo(
                    document("wechat/user/profile/me",
                        requestHeaders(
					        headerWithName("x-csrf-token")
                                .description("CSRF token")
                        ),
                        responseFields(
                            subsectionWithPath("_links").ignored(),//validate by links() block
                            fieldWithPath("nickname")
                                .description("the user's nickname"),
                            fieldWithPath("avatar")
                                .description("uri of the user's avatar")
                        ),
                        links(halLinks(),
                            linkWithRel("self")
                                    .description("link to refresh wechat user profile")
                        )
                    )
                )
        // @formatter:on
    }
}
