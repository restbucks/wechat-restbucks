package org.restbucks.wechat.mp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "wechat.mp")
public class WeChatMpProperties {

    private String appId = "yourAppId";
    private String appSecret = "yourAppSecret";
    private String token = "aRandomString";

}
