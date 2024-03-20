package me.grudin.branchexercise.github;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(GitHubProperties.class)
class GitHubConfiguration {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
