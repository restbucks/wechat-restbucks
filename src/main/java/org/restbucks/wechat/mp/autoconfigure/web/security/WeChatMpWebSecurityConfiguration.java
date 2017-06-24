package org.restbucks.wechat.mp.autoconfigure.web.security;

import me.chanjar.weixin.mp.api.WxMpService;
import org.restbucks.wechat.mp.security.web.RestAuthenticationEntryPoint;
import org.restbucks.wechat.mp.security.web.authentication.WeChatMpOAuth2AuthenticationProcessingFilter;
import org.restbucks.wechat.mp.security.web.authentication.WeChatMpOAuth2AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@Configuration
public class WeChatMpWebSecurityConfiguration {


}
