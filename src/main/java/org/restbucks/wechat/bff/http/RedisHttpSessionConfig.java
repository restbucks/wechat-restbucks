package org.restbucks.wechat.bff.http;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@Configuration
@ConditionalOnProperty(name = "session.mode", havingValue = "redis")
public class RedisHttpSessionConfig {


}
