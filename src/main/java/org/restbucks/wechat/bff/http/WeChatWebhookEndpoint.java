package org.restbucks.wechat.bff.http;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restbucks.wechat.bff.wechat.WeChatMessageDispatcher;
import org.restbucks.wechat.bff.wechat.WeChatRuntime;
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

}
