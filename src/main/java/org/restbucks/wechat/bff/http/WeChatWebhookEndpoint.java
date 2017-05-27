package org.restbucks.wechat.bff.http;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.net.URL;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restbucks.wechat.bff.wechat.WeChatRuntime;
import org.restbucks.wechat.bff.wechat.WeChatUserOauthAccessToken;
import org.restbucks.wechat.bff.wechat.WeChatUserStore;
import org.restbucks.wechat.bff.wechat.messaging.WeChatMessageDispatcher;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WeChatWebhookEndpoint {

    @NonNull
    private final WeChatRuntime weChatRuntime;

    @NonNull
    private final WeChatMessageDispatcher messageDispatcher;

    @NonNull
    private final WeChatUserStore userStore;

    @NonNull
    private final CsrfTokenGenerator csrfTokenGenerator;

    @NonNull
    private final JwtIssuer jwtIssuer;

    @RequestMapping(value = "/webhooks/wechat", method = GET)
    protected String handleAuthentication(@RequestParam String signature,
        @RequestParam String echostr,
        @RequestParam String timestamp,
        @RequestParam String nonce, HttpServletRequest request) {
        log.debug("receiving {}", request.getParameterMap());

        // see http://admin.wechat.com/wiki/index.php?title=Message_Authentication
        return signature.equals(weChatRuntime.calculateSignatureWith(timestamp, nonce)) ? echostr
            : "invalid authentication request";

    }

    @RequestMapping(value = "/webhooks/wechat", method = POST)
    protected void on(@RequestBody String payload) {
        log.debug("receiving {}", payload);
        messageDispatcher.dispatch(payload);
    }

    @RequestMapping(value = "/webhooks/wechat/oauth", method = GET)
    public void onWeChatTellingWhoTheUserIs(@RequestParam("code") final String code,
        @RequestParam("state") final String state,
        HttpServletResponse response) throws IOException {

        WeChatUserOauthAccessToken accessToken = userStore.exchangeAccessTokenWith(code);
        String csrfToken = csrfTokenGenerator.generate();

        String userJwt = jwtIssuer.buildUserJwt(accessToken.getOpenId(), csrfToken);

        URL raw = new URL(state);
        response.addCookie(
            newServerCookie("wechat.restbucks.org.user",
                userJwt, jwtIssuer.getExpiresInSeconds()));
        response.addCookie(
            newClientCookie("wechat.restbucks.org.csrfToken",
                csrfToken, jwtIssuer.getExpiresInSeconds()));
        response.sendRedirect(raw.toString());
    }

    private Cookie newServerCookie(String key, String value, int expiresInSeconds) {
        return newCookie(key, value, true, expiresInSeconds);
    }

    private Cookie newClientCookie(String key, String value, int expiresInSeconds) {
        return newCookie(key, value, false, expiresInSeconds);
    }

    private Cookie newCookie(String key, String value, boolean httpOnly, int expiresInSeconds) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(httpOnly); // against XSS attack
        cookie.setPath("/"); // spike on what is this
        cookie.setMaxAge(expiresInSeconds);
        return cookie;
    }

}
