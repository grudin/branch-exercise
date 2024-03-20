package me.grudin.branchexercise.github;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.Instant;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
record GitHubGetUserResponse(
    String login,
    Long id,
    String avatarUrl,
    String location,
    String email,
    String url,
    Instant createdAt,
    String reposUrl
) {}
