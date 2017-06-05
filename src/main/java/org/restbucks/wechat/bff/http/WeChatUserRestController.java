package org.restbucks.wechat.bff.http;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restbucks.wechat.bff.http.assembler.WeChatUserProfileResourceAssembler;
import org.restbucks.wechat.bff.http.resource.WeChatUserProfileResource;
import org.restbucks.wechat.bff.http.security.CurrentWeChatUser;
import org.restbucks.wechat.bff.wechat.oauth.OpenId;
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserProfile;
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WeChatUserRestController {

    @NonNull
    private final WeChatUserProfileResourceAssembler userResourceAssembler;

    @NonNull
    private final WeChatUserStore userStore;

    @RequestMapping(value = "/rel/wechat/user/profile/me", method = GET)
    public WeChatUserProfileResource me(@CurrentWeChatUser OpenId openId) {
        WeChatUserProfile userProfile = userStore.findUserProfile(openId);
        return userResourceAssembler.toResource(userProfile);
    }


}
