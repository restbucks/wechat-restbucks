package org.restbucks.wechat.bff.http.security;

import static java.util.Collections.emptyList;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String jwt;
    private final String csrfToken;

    public JwtAuthenticationToken(String jwt, String csrfToken) {
        super(emptyList());
        this.jwt = jwt;
        this.csrfToken = csrfToken;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return jwt;
    }
}
