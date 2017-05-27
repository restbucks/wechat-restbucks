package org.restbucks.wechat.bff.wechat;

import lombok.Value;

@Value
public class OpenId {

    private String value;

    private OpenId(String value) {
        this.value = value;
    }

    public static OpenId valueOf(String value) {
        return new OpenId(value);
    }
}
