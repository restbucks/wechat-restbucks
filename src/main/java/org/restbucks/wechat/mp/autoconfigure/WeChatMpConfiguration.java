package org.restbucks.wechat.mp.autoconfigure;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpKefuService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.restbucks.wechat.mp.autoconfigure.web.WeChatMpWebConfiguration;
import org.restbucks.wechat.mp.autoconfigure.web.WeChatMpWebMethodConfiguration;
import org.restbucks.wechat.mp.autoconfigure.web.security.WeChatMpWebSecurityConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(WeChatMpProperties.class)
@Import(
    {
        WeChatMpWebConfiguration.class,
        WeChatMpWebSecurityConfiguration.class,
        WeChatMpWebMethodConfiguration.class
    }
)
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
    protected WxMpService wxMpService(WxMpConfigStorage wxMpConfigStorage) {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
        return wxMpService;
    }

    @Bean
    protected WxMpKefuService wxMpKefuService(WxMpService wxMpService) {
        return wxMpService.getKefuService();
    }

    @Bean
    protected WxMpUserService wxMpUserService(WxMpService wxMpService) {
        return wxMpService.getUserService();
    }
}
