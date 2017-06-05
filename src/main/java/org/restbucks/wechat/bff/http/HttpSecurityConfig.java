package org.restbucks.wechat.bff.http;

import static org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.restbucks.wechat.bff.http.security.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class HttpSecurityConfig extends WebSecurityConfigurerAdapter {

    @NonNull
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
        throws Exception {
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.antMatcher("/**")
            .authorizeRequests()
                .antMatchers("/rel/**/me").authenticated()
                .anyRequest().permitAll()
            .and()
                .sessionManagement().sessionCreationPolicy(IF_REQUIRED)
            .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint);
        // @formatter:on
    }
}
