package org.restbucks.wechat.bff.http.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserOauthAccessToken;
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserStore;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class WeChatOauthCallbackFilter extends AbstractAuthenticationProcessingFilter {

    @Setter
    private WeChatUserStore userStore;

    public WeChatOauthCallbackFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public WeChatOauthCallbackFilter(
        RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {

        final String code = request.getParameter("code");

        WeChatUserOauthAccessToken accessToken = userStore.exchangeAccessTokenWith(code);

        return new WeChatUserAdapter(accessToken.getOpenId());
    }
}
