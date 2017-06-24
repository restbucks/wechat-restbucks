package org.restbucks.wechat.mp.autoconfigure.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.restbucks.wechat.mp.autoconfigure.WeChatMpConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RUNTIME)
@Target(
    {
        TYPE
    }
)
@Documented
@Import(WeChatMpConfiguration.class)
@Configuration
public @interface EnableWeChatMpApplication {

}
