package me.grudin.branchexercise.github;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@ConfigurationProperties("github")
@Validated
class GitHubProperties {

    @NotBlank
    private String baseUrl;
}
