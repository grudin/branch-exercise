package me.grudin.branchexercise.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.Instant;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
record User(
    String userName,
    String displayName,
    String avatar,
    String geoLocation,
    String email,
    String url,
    Instant createdAt
) {}
