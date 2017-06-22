package org.restbucks.wechat.mp;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpKefuService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WeChatMpProperties.class)
public class WeChatMpConfiguration {

    @Bean
    @ConditionalOnMissingBean
    protected WxMpConfigStorage configStorage(WeChatMpProperties weChatMpProperties) {

        WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
        configStorage.setAppId(weChatMpProperties.getAppId());
        configStorage.setSecret(weChatMpProperties.getAppSecret());
        configStorage.setToken(weChatMpProperties.getToken());
        return configStorage;
    }

    @Bean
    @ConditionalOnMissingBean
    public WxMpService wxMpService(WxMpConfigStorage wxMpConfigStorage) {
        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
        return wxMpService;
    }

    @Bean
    public WxMpKefuService wxMpKefuService(WxMpService wxMpService) {
        return wxMpService.getKefuService();
    }

    @Bean
    public WxMpUserService wxMpUserService(WxMpService wxMpService) {
        return wxMpService.getUserService();
    }
}
