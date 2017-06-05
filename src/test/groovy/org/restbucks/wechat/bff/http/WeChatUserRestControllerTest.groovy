package org.restbucks.wechat.bff.http

import org.junit.Test
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserProfileFixture

import static org.mockito.BDDMockito.given
import static org.restbucks.wechat.bff.http.WeChatUserRequestPostProcessor.weChatUser
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.payload.PayloadDocumentation.*
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class WeChatUserRestControllerTest extends AbstractWebMvcTest {

    @Test
    void returns_wechat_user_profile() {

        def userProfile = new WeChatUserProfileFixture().build()

        given(weChatUserStore.findUserProfile(userProfile.openId)).willReturn(userProfile)

        // @formatter:off
        this.mockMvc.perform(
                    get("/rel/wechat/user/profile/me")
                    .with(weChatUser().with(userProfile.openId)).with(csrf().asHeader())
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
