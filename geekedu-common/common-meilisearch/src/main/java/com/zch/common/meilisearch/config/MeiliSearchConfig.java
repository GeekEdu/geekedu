package com.zch.common.meilisearch.config;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Poison02
 * @date 2024/4/2
 */
@Configuration
public class MeiliSearchConfig {

    //@Value("${geekedu.search.search-host}")
    private String searchHost = "http:localhost:7700";

    //@Value("${geekedu.search.search-api-key}")
    private String searchApiKey = "vGYHVruzupdS6SBD_Nrv3F8hnp4JETgp824odEZc5vA";

    @Bean
    @ConditionalOnMissingBean
    public Client client() {
        return new Client(new Config(searchHost, searchApiKey));
    }

}
