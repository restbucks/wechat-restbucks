package org.restbucks.wechat.bff.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "wechat")
public class WeChatRuntime {

    private String appId = "yourAppId";
    private String appSecret = "yourAppSecret";
    private String token = "aRandomString";
    private String apiBaseUri = "https://api.wechat.com";

}
