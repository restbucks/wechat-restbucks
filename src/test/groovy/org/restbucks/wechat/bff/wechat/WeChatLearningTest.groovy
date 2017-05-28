package org.restbucks.wechat.bff.wechat

import groovy.json.JsonSlurper
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.RestTemplate

@RunWith(SpringRunner)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "dev")
class WeChatLearningTest {

    private RestTemplate restTemplate = new RestTemplate()

    def jsonSlurper = new JsonSlurper()

    @Autowired
    private WeChatRuntime weChatRuntime

    @Test
    void get_account_access_token() {
        String accessToken = getAccessToken()

        println(accessToken)
    }

    def getAccessToken() {

        def accessToken = restTemplate.getForObject("https://api.wechat.com/cgi-bin/token?grant_type=client_credential&appid={appId}&secret={appSecret}",
                String.class, weChatRuntime.appId, weChatRuntime.appSecret)
        return jsonSlurper.parseText(accessToken)
    }

    @Test
    void generate_qrcode() {
        def ticket = restTemplate.postForObject("https://api.wechat.com/cgi-bin/qrcode/create?access_token={token}",
                """
                    {
                        "action_name": "QR_LIMIT_STR_SCENE", 
                        "action_info": { 
                            "scene": {
                                "scene_str": "STORE_125"
                            }
                        }
                    }
                """,
                String.class, getAccessToken().access_token
        )
        println(ticket)

        //https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket={ticket}
    }
}
