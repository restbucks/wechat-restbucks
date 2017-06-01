package org.restbucks.wechat.bff.http.security;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Collection;
import java.util.Collections;
import org.restbucks.wechat.bff.wechat.oauth.OpenId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@SuppressFBWarnings(value = "Se", justification = "Why serializable?")
public class WeChatUserAdapter implements Authentication {

    private final OpenId openId;

    public WeChatUserAdapter(OpenId openId) {
        this.openId = openId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return openId;
    }

    @Override
    public Object getPrincipal() {
        return openId;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return openId.getValue();
    }
}