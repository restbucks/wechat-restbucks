package org.restbucks.wechat.bff.http;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restbucks.wechat.bff.AppRuntime;
import org.restbucks.wechat.bff.wechat.WeChatRuntime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WeChatOauthProxy {

    @NonNull
    private final AppRuntime appRuntime;

    @NonNull
    private final WeChatRuntime weChatRuntime;

    @RequestMapping(value = "/wechat/oauth/authorize", method = GET)
    public void askWeChatWhoTheUserIs(@RequestParam(name = "origin") String origin,
        HttpServletResponse response) throws IOException {

        final String endpointUrl = appRuntime.getPublicUri("/webhooks/wechat/oauth");

        String redirect = format(
            "https://open.weixin.qq.com/connect/oauth2/authorize"
                + "?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect",
            weChatRuntime.getAppId(),
            URLEncoder.encode(endpointUrl, "UTF-8"),
            "snsapi_base", // cannot get user profile with "snsapi_base", got unauthorized
            URLEncoder.encode(origin, "UTF-8"));

        log.debug("We don't know who u are, redirecting you from {} to {}", origin, redirect);
        response.sendRedirect(redirect);
    }

}
