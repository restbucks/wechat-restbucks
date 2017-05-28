package org.restbucks.wechat.bff.wechat.messaging;

import static java.util.stream.Collectors.toList;

import com.google.common.base.Joiner;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.restbucks.wechat.bff.wechat.WeChatApiAccessTokenStore;
import org.restbucks.wechat.bff.wechat.WeChatRuntime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class WeChatMessageSender {

    private final RestTemplate restTemplate;

    private final WeChatRuntime weChatRuntime;

    private final WeChatApiAccessTokenStore apiAccessTokenStore;

    public WeChatMessageSender(@Qualifier("wechat.RestTemplate") RestTemplate restTemplate,
        WeChatRuntime weChatRuntime,
        WeChatApiAccessTokenStore apiAccessTokenStore) {
        this.restTemplate = restTemplate;
        this.weChatRuntime = weChatRuntime;
        this.apiAccessTokenStore = apiAccessTokenStore;
    }

    public void send(NewsMessage message) {

        final String articleFormat = "{"
            + "        \"title\": \"%s\","
            + "        \"description\": \"%s\","
            + "        \"url\": \"%s\","
            + "        \"picurl\": \"%s\""
            + "      }";

        Stream<String> articleStream = message.articles()
            .map(article -> String.format(articleFormat,
                article.getTitle(),
                article.getDescription(),
                article.getUrl(),
                article.getImage()));

        String articles = Joiner.on(",").join(articleStream.collect(toList()));

        final String body = String.format("{"
            + "  \"touser\": \"" + message.getRecipient() + "\","
            + "  \"msgtype\": \"news\","
            + "  \"news\": {"
            + "    \"articles\": [%s]"
            + "  }"
            + "}", articles);

        String response = restTemplate
            .postForObject(
                "/cgi-bin/message/custom/send?access_token={token}",
                body,
                String.class,
                apiAccessTokenStore.get().getAccessToken()
            );
        log.debug(response);
    }
}
