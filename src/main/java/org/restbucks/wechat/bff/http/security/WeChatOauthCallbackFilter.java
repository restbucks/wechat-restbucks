package org.restbucks.wechat.bff.http.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.SneakyThrows;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.restbucks.wechat.bff.wechat.oauth.OpenId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class WeChatOauthCallbackFilter extends AbstractAuthenticationProcessingFilter {

    @Setter
    private WxMpService wxMpService;

    public WeChatOauthCallbackFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public WeChatOauthCallbackFilter(
        RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {

        final String code = request.getParameter("code");

        WxMpOAuth2AccessToken accessToken = wxMpService.oauth2getAccessToken(code);

        return new WeChatUserAdapter(accessToken);
    }
}
