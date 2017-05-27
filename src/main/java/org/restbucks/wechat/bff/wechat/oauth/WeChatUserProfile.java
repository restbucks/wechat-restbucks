package org.restbucks.wechat.bff.wechat.oauth;

import static lombok.AccessLevel.PROTECTED;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @JsonIgnore
    @JsonSerialize(using = OpenIdSerializer.class)
    private OpenId openId;

    private String nickname;

    private String avatar;

    public WeChatUserProfile(OpenId openId, String nickname, String avatar) {
        this.openId = openId;
        this.nickname = nickname;
        this.avatar = avatar;
    }
}
