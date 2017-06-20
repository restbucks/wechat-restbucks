package org.restbucks.wechat.bff.http;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WeChatWebhookEndpoint {

    @NonNull
    private final WxMpService weChatRuntime;

    @NonNull
    private final WxMpMessageRouter wxMpMessageRouter;

    @RequestMapping(value = "/webhooks/wechat/messaging", method = GET)
    protected String handleAuthentication(@RequestParam String signature,
        @RequestParam String echostr,
        @RequestParam String timestamp,
        @RequestParam String nonce, HttpServletRequest request) {
        log.debug("receiving {}", request.getParameterMap());

        // see http://admin.wechat.com/wiki/index.php?title=Message_Authentication
        return weChatRuntime.checkSignature(timestamp, nonce, signature) ? echostr
            : "invalid authentication request";

    }

    @RequestMapping(value = "/webhooks/wechat/messaging", method = POST)
    protected WxMpXmlOutMessage on(@RequestBody String payload) {
        log.debug("receiving {}", payload);
        return wxMpMessageRouter.route(WxMpXmlMessage.fromXml(payload));
    }

}
