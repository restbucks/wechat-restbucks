package org.restbucks.wechat.bff.http;

import org.restbucks.wechat.bff.http.security.CurrentWeChatUser;
import org.restbucks.wechat.bff.wechat.oauth.OpenId;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class OpenIdHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(OpenId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
        ModelAndViewContainer modelAndViewContainer,
        NativeWebRequest nativeWebRequest,
        WebDataBinderFactory webDataBinderFactory) throws Exception {
        CurrentWeChatUser weChatUser = methodParameter
            .getParameterAnnotation(CurrentWeChatUser.class);
        if (weChatUser == null) {
            return null;
        }

        return SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
    }
}