package org.restbucks.wechat.bff.http.security;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Collection;
import java.util.Collections;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.restbucks.wechat.bff.wechat.oauth.OpenId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@SuppressFBWarnings(value = "Se", justification = "Why serializable?")
public class WeChatOAuth2AccessTokenAdapter implements Authentication {

    private final WxMpOAuth2AccessToken accessToken;

    public WeChatOAuth2AccessTokenAdapter(WxMpOAuth2AccessToken accessToken) {
        this.accessToken = accessToken;
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
        return getOpenId();
    }

    @Override
    public Object getPrincipal() {
        return getOpenId();
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
        return accessToken.getOpenId();
    }

    public OpenId getOpenId() {
        return OpenId.valueOf(accessToken.getOpenId());
    }
}
