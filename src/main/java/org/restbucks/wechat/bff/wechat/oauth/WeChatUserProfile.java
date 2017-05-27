package org.restbucks.wechat.bff.wechat.oauth;

import static lombok.AccessLevel.PROTECTED;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(of = "openId")
@NoArgsConstructor(access = PROTECTED)
@Setter(PROTECTED)
public class WeChatUserProfile {
    @JsonProperty("openid")
    @JsonDeserialize(using = OpenIdDeserializer.class)
    private OpenId openId;

    private String nickname;

    @JsonProperty("headimgurl")
    private String avatar;

    public WeChatUserProfile(OpenId openId, String nickname, String avatar) {
        this.openId = openId;
        this.nickname = nickname;
        this.avatar = avatar;
    }
}
