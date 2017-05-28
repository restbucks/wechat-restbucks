package org.restbucks.wechat.bff.wechat.messaging;

import static java.lang.String.format;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.Setter;
import lombok.SneakyThrows;
import org.restbucks.wechat.bff.wechat.messaging.NewsMessage.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class WeChatMessageDispatcher {

    private final WeChatMessageSender messageSender;

    private final ObjectMapper xmlMapper;

    @Autowired
    public WeChatMessageDispatcher(
        WeChatMessageSender messageSender,
        @Qualifier("wechat.XmlMapper") ObjectMapper xmlMapper) {
        this.messageSender = messageSender;
        this.xmlMapper = xmlMapper;
    }

    @Setter
    private String publicBaseUri = "https://wechat.restbucks.org";

    @SneakyThrows
    public void dispatch(String payload) {
        QrCodeScannedEventMessage inboundMessage = xmlMapper
            .readValue(payload, QrCodeScannedEventMessage.class);
        String storeNumber = getStore(inboundMessage);
        messageSender
            .send(new NewsMessage(inboundMessage.getFromUserName(),
                Article.builder()
                    .title(format("Welcome to Restbucks Store %s", storeNumber))
                    .url(publicBaseUri + "/index.html#/place-order-form/" + storeNumber)));
    }

    private String getStore(QrCodeScannedEventMessage inboundMessage) {
        return Optional.ofNullable(inboundMessage.getEventKey()) // "qrscene_store_123"
            .map(k -> k.substring(k.indexOf("_") + 1)) // getting "store_123"
            .map(k -> k.substring(k.indexOf("_") + 1)) // getting "123"
            .orElseThrow(() -> new IllegalArgumentException(
                "Cannot extract store number due to empty qr code key"));
    }
}
