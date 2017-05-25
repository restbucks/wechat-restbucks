package org.restbucks.wechat.bff.wechat.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Value;


@JsonRootName("xml")
@Value
public class QrCodeScannedEventMessage {

    @JsonProperty("FromUserName")
    private String fromUserName;

    @JsonProperty("EventKey")
    private String eventKey;
}
