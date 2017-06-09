package org.restbucks.wechat.bff.http;

import com.hazelcast.config.Config;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.config.MapAttributeConfig;
import com.hazelcast.config.MapIndexConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.hazelcast.HazelcastSessionRepository;
import org.springframework.session.hazelcast.PrincipalNameExtractor;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;

@Configuration
@EnableHazelcastHttpSession
@ConditionalOnProperty(name = "session.mode", havingValue = "hazelcast")
public class HazelcastHttpSessionConfig {

    @Bean
    public HazelcastInstance hazelcastInstance() {
        MapAttributeConfig attributeConfig = new MapAttributeConfig()
            .setName(HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
            .setExtractor(PrincipalNameExtractor.class.getName());

        Config config = new Config();

        NetworkConfig network = config.getNetworkConfig();
        InterfacesConfig interfacesConfig = network.getInterfaces();
        interfacesConfig.setEnabled(true)
            .addInterface("10.208.20.*");

        config.getMapConfig("spring:session:sessions")
            .addMapAttributeConfig(attributeConfig)
            .addMapIndexConfig(new MapIndexConfig(
                HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE, false));

        return Hazelcast.newHazelcastInstance(config);
    }
}
