package me.grudin.branchexercise.github;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@ConfigurationProperties("github")
class GitHubProperties {

    private String baseUrl;
}
