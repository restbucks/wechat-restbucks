package org.restbucks.wechat.bff.wechat.oauth;

import java.io.Serializable;
import lombok.Value;

@Value
public class OpenId implements Serializable {

    private String value;

    private OpenId(String value) {
        this.value = value;
    }

    public static OpenId valueOf(String value) {
        return new OpenId(value);
    }
}
