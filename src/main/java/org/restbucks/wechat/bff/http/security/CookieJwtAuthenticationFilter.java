package org.restbucks.wechat.bff.http.security;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class CookieJwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public CookieJwtAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        String csrfToken = request.getHeader("x-csrf-token");

        Cookie[] cookies = request.getCookies();

        JwtAuthenticationToken userJwt = Arrays.stream(cookies == null ? new Cookie[] {} : cookies)
            .filter(cookie -> cookie.getName().equals("wechat.restbucks.org.user"))
            .findFirst()
            .map(cookie -> new JwtAuthenticationToken(cookie.getValue(), csrfToken))
            .orElse(new JwtAuthenticationToken("", csrfToken));//null token which will fail

        return getAuthenticationManager().authenticate(userJwt);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        // As this authentication is in HTTP header,
        // after success we need to continue the request normally
        // and return the response as if the resource was not secured at all
        chain.doFilter(request, response);
    }
}
