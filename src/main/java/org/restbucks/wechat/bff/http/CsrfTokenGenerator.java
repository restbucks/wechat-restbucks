package org.restbucks.wechat.bff.http;

import java.security.SecureRandom;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class CsrfTokenGenerator {
    public String generate() {
        byte[] bytes = new byte[256]; //Means 2048 bit
        new SecureRandom().nextBytes(bytes);
        return Base64.encodeBase64String(bytes);
    }
}
