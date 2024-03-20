package me.grudin.branchexercise.github;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
class GitHubClientImpl implements GitHubClient {

    private final RestTemplate restTemplate;
    private final GitHubProperties gitHubProperties;

    @Override
    public GitHubGetUserResponse getUser(String username) {
        if (StringUtils.isBlank(username)) {
            log.warn("Attempted to retrieve a user without a username.");
            return null;
        }

        URI uri = UriComponentsBuilder.fromHttpUrl(gitHubProperties.getBaseUrl())
            .path("/users/{username}")
            .buildAndExpand(username)
            .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, "application/vnd.github+json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            return restTemplate.exchange(uri, HttpMethod.GET, requestEntity, GitHubGetUserResponse.class).getBody();
        } catch (RestClientResponseException e) {
            log.error("Unable to get user: {}", username, e);
            return null;
        }
    }
}
