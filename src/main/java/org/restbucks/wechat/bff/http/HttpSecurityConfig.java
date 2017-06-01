package org.restbucks.wechat.bff.http;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.Collections;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.restbucks.wechat.bff.http.security.CookieJwtAuthenticationFilter;
import org.restbucks.wechat.bff.http.security.JwtAuthenticationProvider;
import org.restbucks.wechat.bff.http.security.RestAuthenticationEntryPoint;
import org.restbucks.wechat.bff.http.security.RestAuthenticationSuccessHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class HttpSecurityConfig extends WebSecurityConfigurerAdapter {

    @NonNull
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @NonNull
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
        throws Exception {
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests()
                .anyRequest().permitAll()
            .and()
                .sessionManagement().sessionCreationPolicy(STATELESS)
            .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
            .and()
                .addFilterBefore(jwtAuthenticationFilter("/rel/**/me"),
                    BasicAuthenticationFilter.class);
        // @formatter:on
    }

    private CookieJwtAuthenticationFilter jwtAuthenticationFilter(
        String defaultFilterProcessesUrl) {
        CookieJwtAuthenticationFilter filter = new CookieJwtAuthenticationFilter(
            defaultFilterProcessesUrl);
        filter.setAuthenticationSuccessHandler(mySuccessHandler());
        filter.setAuthenticationManager(
            new ProviderManager(Collections.singletonList(jwtAuthenticationProvider)));
        return filter;
    }

    private RestAuthenticationSuccessHandler mySuccessHandler() {
        return new RestAuthenticationSuccessHandler();
    }
}
