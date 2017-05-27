package org.restbucks.wechat.bff.wechat;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;

@Configuration
@RequiredArgsConstructor
public class WeChatConfig {

    @NonNull
    private final WeChatRuntime weChatRuntime;

    @Bean
    protected XmlMapper xmlMapperForWeChatMessage() {
        XmlMapper objectMapper = new XmlMapper();
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean(name = "wechat.RestTemplate")
    public RestTemplate restTemplate() {
        DefaultUriTemplateHandler uriTemplateHandler = new DefaultUriTemplateHandler();
        uriTemplateHandler.setBaseUrl(weChatRuntime.getApiBaseUri());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(uriTemplateHandler);
        return restTemplate;
    }
}
