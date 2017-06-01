package org.restbucks.wechat.bff.http

import org.junit.Test

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.payload.PayloadDocumentation.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class IndexRestControllerTest extends AbstractWebMvcTest {

    @Test
    void list_rels() {

        // @formatter:off
        this.mockMvc.perform(
                    get("/index")
                )
	            .andExpect(status().isOk())
                .andDo(print())
	            .andDo(
                    document("index",
                        responseFields(
                            fieldWithPath("content").ignored(),
                            subsectionWithPath("_links").ignored()//validate by links() block
                        ),
                        links(halLinks(),
                            linkWithRel("self").description("link to refresh index")
                        )
                    )
                )
        // @formatter:on
    }
}
