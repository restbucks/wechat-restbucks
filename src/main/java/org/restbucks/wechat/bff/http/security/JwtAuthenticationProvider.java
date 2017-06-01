package org.restbucks.wechat.bff.http.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.restbucks.wechat.bff.wechat.oauth.OpenId;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @NonNull
    private final JwtIssuer jwtIssuer;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        try {
            OpenId openId = jwtIssuer.verified(token.getJwt(), token.getCsrfToken());
            return new WeChatUserAdapter(openId);
        } catch (CannotVerifyJwtException exception) {
            throw new AuthenticationException(exception.getMessage(), exception) {

            };
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
