package org.restbucks.wechat.bff.http;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@RequiredArgsConstructor
public class HttpMvcConfig extends WebMvcConfigurerAdapter {

    @NonNull
    private final OpenIdHandlerMethodArgumentResolver openIdHandlerMethodArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(openIdHandlerMethodArgumentResolver);
    }

}
