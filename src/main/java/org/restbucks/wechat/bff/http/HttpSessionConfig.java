package org.restbucks.wechat.bff.http;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

//@EnableRedisHttpSession
@Configuration
@EnableSpringHttpSession
@ConditionalOnProperty(name = "session.mode", havingValue = "map")
public class HttpSessionConfig {


    @Bean
    protected SessionRepository sessionRepository() {
        return new MapSessionRepository();
    }
}
