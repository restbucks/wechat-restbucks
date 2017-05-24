package org.restbucks.wechat.bff.wechat;

import static ch.qos.logback.core.CoreConstants.EMPTY_STRING;

import java.util.stream.Stream;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wechat")
@Data
public class WeChatRuntime {

    private String token = "aRandomString";

    public String calculateSignatureWith(String timestamp, String nonce) {
        return DigestUtils.sha1Hex(
            Stream.of(getToken(), timestamp, nonce).sorted()
                .reduce(EMPTY_STRING, String::concat)
        );
    }
}
