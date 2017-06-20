package org.restbucks.wechat.bff.wechat.messaging;

import static java.lang.String.format;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Optional;
import lombok.Setter;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.restbucks.wechat.bff.wechat.messaging.NewsMessage.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class QrCodeScannedEventHandler implements WxMpMessageHandler {

    private final WeChatMessageSender messageSender;

    private final ObjectMapper xmlMapper;

    @Autowired
    public QrCodeScannedEventHandler(
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
        String eventKey = inboundMessage.getEventKey();
        String fromUser = inboundMessage.getFromUserName();
        handle(eventKey, fromUser);
    }

    private void handle(String eventKey, String fromUser) {
        String storeNumber = getStore(eventKey);
        messageSender
            .send(new NewsMessage(fromUser,
                Article.builder()
                    .title(format("Welcome to Restbucks Store %s", storeNumber))
                    .url(publicBaseUri + "/index.html#/place-order-form/" + storeNumber)));
    }

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
        WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {

        handle(wxMessage.getEventKey(), wxMessage.getFromUser());

        return null;
    }

    private String getStore(String eventKey) {
        return Optional.ofNullable(eventKey) // "qrscene_store_123"
            .map(k -> k.substring(k.indexOf("_") + 1)) // getting "store_123"
            .map(k -> k.substring(k.indexOf("_") + 1)) // getting "123"
            .orElseThrow(() -> new IllegalArgumentException(
                "Cannot extract store number due to empty qr code key"));
    }
}
