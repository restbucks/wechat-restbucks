package org.restbucks.wechat.bff.http;

import static org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.restbucks.wechat.bff.http.security.RestAuthenticationEntryPoint;
import org.restbucks.wechat.bff.http.security.WeChatOauthAuthenticationSuccessHandler;
import org.restbucks.wechat.bff.http.security.WeChatOauthCallbackFilter;
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class HttpSecurityConfig extends WebSecurityConfigurerAdapter {

    @NonNull
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @NonNull
    private final WeChatUserStore weChatUserStore;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
        throws Exception {
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        CookieCsrfTokenRepository cookieCsrfTokenRepository =
            CookieCsrfTokenRepository.withHttpOnlyFalse();
        cookieCsrfTokenRepository.setCookieName("wechat.restbucks.org.csrfToken");

        http.antMatcher("/**")
            .authorizeRequests()
                .antMatchers("/rel/**/me").authenticated()
                .anyRequest().permitAll()
            .and()
                .sessionManagement().sessionCreationPolicy(IF_REQUIRED)
            .and()
                .csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("/rel/**/me"))
                .csrfTokenRepository(csrfTokenRepository())
            .and()
                .addFilterAfter(weChatOauthCallbackFilter("/webhooks/wechat/oauth"),
                    CsrfFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint);
        // @formatter:on
    }

    @Bean
    protected CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository cookieCsrfTokenRepository = CookieCsrfTokenRepository
            .withHttpOnlyFalse();
        cookieCsrfTokenRepository.setCookieName("wechat.restbucks.org.csrfToken");
        return cookieCsrfTokenRepository;
    }

    @Bean
    protected CsrfAuthenticationStrategy sessionAuthenticationStrategy() {
        return new CsrfAuthenticationStrategy(csrfTokenRepository());
    }

    private WeChatOauthCallbackFilter weChatOauthCallbackFilter(String url) {
        WeChatOauthCallbackFilter filter = new WeChatOauthCallbackFilter(url);
        filter.setUserStore(weChatUserStore);
        filter.setAuthenticationSuccessHandler(new WeChatOauthAuthenticationSuccessHandler());
        filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());
        return filter;
    }

}
