package me.grudin.branchexercise.github;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.Instant;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GitHubUser(
    String login,
    Long id,
    String avatarUrl,
    String htmlUrl,
    String reposUrl,
    String name,
    String location,
    String email,
    Instant createdAt
) {}
