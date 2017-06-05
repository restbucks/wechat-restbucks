package org.restbucks.wechat.bff.http;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restbucks.wechat.bff.http.security.WeChatUserAdapter;
import org.restbucks.wechat.bff.wechat.WeChatRuntime;
import org.restbucks.wechat.bff.wechat.messaging.WeChatMessageDispatcher;
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserOauthAccessToken;
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserStore;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @RequestMapping(value = "/webhooks/wechat/messaging", method = GET)
    protected String handleAuthentication(@RequestParam String signature,
        @RequestParam String echostr,
        @RequestParam String timestamp,
        @RequestParam String nonce, HttpServletRequest request) {
        log.debug("receiving {}", request.getParameterMap());

        // see http://admin.wechat.com/wiki/index.php?title=Message_Authentication
        return signature.equals(weChatRuntime.calculateSignatureWith(timestamp, nonce)) ? echostr
            : "invalid authentication request";

    }

    @RequestMapping(value = "/webhooks/wechat/messaging", method = POST)
    protected void on(@RequestBody String payload) {
        log.debug("receiving {}", payload);
        messageDispatcher.dispatch(payload);
    }

    @RequestMapping(value = "/webhooks/wechat/oauth", method = GET)
    public void onWeChatTellingWhoTheUserIs(@RequestParam("code") final String code,
        @RequestParam("state") final String state,
        HttpServletRequest request,
        HttpServletResponse response) throws IOException {

        WeChatUserOauthAccessToken accessToken = userStore.exchangeAccessTokenWith(code);

        SecurityContextHolder.getContext()
            .setAuthentication(new WeChatUserAdapter(accessToken.getOpenId()));

        URL raw = new URL(state);
        response.sendRedirect(raw.toString());
    }
}
