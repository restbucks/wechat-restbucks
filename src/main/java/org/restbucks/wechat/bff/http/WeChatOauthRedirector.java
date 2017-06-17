package org.restbucks.wechat.bff.http;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import org.restbucks.wechat.bff.AppRuntime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WeChatOauthRedirector {

    @NonNull
    private final AppRuntime appRuntime;

    @NonNull
    private final WxMpService weChatMpService;

    @RequestMapping(value = "/wechat/oauth/authorize", method = GET)
    public void askWeChatWhoTheUserIs(@RequestParam(name = "origin") String origin,
        HttpServletRequest request,
        HttpServletResponse response) throws IOException {

        final String endpointUrl = appRuntime.getPublicUri("/webhooks/wechat/oauth");

        String base64EncodedOrigin =
            Base64.getUrlEncoder().encodeToString(origin.getBytes(Charset.forName("UTF-8")));

        String redirect = weChatMpService
            .oauth2buildAuthorizationUrl(endpointUrl, "snsapi_base", base64EncodedOrigin);

        log.debug("We don't know who u are, redirecting you from {} to {}", origin, redirect);
        response.sendRedirect(redirect);
    }

}
