package org.restbucks.wechat.bff.http;

import lombok.RequiredArgsConstructor;
import org.restbucks.wechat.mp.autoconfigure.web.security.WeChatMpWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class HttpSecurityConfig extends WeChatMpWebSecurityConfigurerAdapter {


}
