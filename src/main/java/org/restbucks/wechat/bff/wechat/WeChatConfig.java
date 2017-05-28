package org.restbucks.wechat.bff.wechat;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.util.Collections.singletonList;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.nio.charset.Charset;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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

        MappingJackson2HttpMessageConverter plainTextToJsonConverter =
            new MappingJackson2HttpMessageConverter();

        plainTextToJsonConverter.setDefaultCharset(Charset.forName("UTF-8"));
        plainTextToJsonConverter.setSupportedMediaTypes(
            singletonList(MediaType.TEXT_PLAIN)); // can't believe it is not application/json

        restTemplate.getMessageConverters().add(plainTextToJsonConverter);

        return restTemplate;
    }
}
