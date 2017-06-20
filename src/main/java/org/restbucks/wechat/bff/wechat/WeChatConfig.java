package org.restbucks.wechat.bff.wechat;

import static java.util.Collections.singletonList;

import java.nio.charset.Charset;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpKefuService;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.restbucks.wechat.bff.wechat.messaging.QrCodeScannedEventHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(WeChatRuntime.class)
public class WeChatConfig {

    @Bean
    protected WeChatRuntime weChatRuntime() {
        return new WeChatRuntime();
    }

    @Bean
    @ConditionalOnMissingBean
    protected WxMpConfigStorage configStorage() {
        WeChatRuntime weChatRuntime = weChatRuntime();

        WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
        configStorage.setAppId(weChatRuntime.getAppId());
        configStorage.setSecret(weChatRuntime.getAppSecret());
        configStorage.setToken(weChatRuntime.getToken());
        return configStorage;
    }

    @Bean
    @ConditionalOnMissingBean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(configStorage());
        return wxMpService;
    }

    @Bean
    public WxMpKefuService wxKefuService(WxMpService wxMpService) {
        return wxMpService.getKefuService();
    }

    @Bean
    public WxMpMessageRouter wxMpMessageRouter(
        QrCodeScannedEventHandler qrCodeScannedEventHandler) {
        // @formatter:off
        WxMpMessageRouter router = new WxMpMessageRouter(wxMpService());
        router
            .rule()
                .msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_SCAN)
                .handler(qrCodeScannedEventHandler)
            .end();
        // @formatter:on
        return router;
    }

    @Bean(name = "wechat.RestTemplate")
    public RestTemplate restTemplate(WeChatRuntime weChatRuntime) {
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
