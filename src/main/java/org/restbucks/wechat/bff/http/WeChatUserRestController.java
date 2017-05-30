package org.restbucks.wechat.bff.http;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restbucks.wechat.bff.http.assembler.WeChatUserProfileResourceAssembler;
import org.restbucks.wechat.bff.http.resource.WeChatUserProfileResource;
import org.restbucks.wechat.bff.http.security.JwtIssuer;
import org.restbucks.wechat.bff.wechat.oauth.OpenId;
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserProfile;
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserStore;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WeChatUserRestController {

    @NonNull
    private final JwtIssuer jwtIssuer;

    @NonNull
    private final WeChatUserProfileResourceAssembler userResourceAssembler;

    @NonNull
    private final WeChatUserStore userStore;

    @RequestMapping(value = "/rel/wechat/user/profile/me", method = GET)
    public WeChatUserProfileResource me(
        @CookieValue(value = "wechat.restbucks.org.user", required = false) String userJwt,
        @RequestHeader(value = "x-csrf-token", required = false) String csrfToken) {
        OpenId openId = jwtIssuer.verified(userJwt, csrfToken);
        WeChatUserProfile userProfile = userStore.findUserProfile(openId);
        return userResourceAssembler.toResource(userProfile);
    }


}
