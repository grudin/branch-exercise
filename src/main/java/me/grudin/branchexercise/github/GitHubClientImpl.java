package me.grudin.branchexercise.github;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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
            throw new IllegalArgumentException("Username must not be blank.");
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
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                log.error("User: {} not found.", username, e);
                throw new GitHubResourceNotFoundException(e);
            }

            log.error("Unable to get user: {}", username, e);
            throw new GitHubClientException(e);
        } catch (HttpServerErrorException e) {
            log.error("Unable to get user: {}", username, e);
            throw new GitHubServerException(e);
        }
    }

    @Override
    public List<GitHubRepo> getRepos(String username) {
        if (StringUtils.isBlank(username)) {
            log.warn("Attempted to retrieve a user's repos without a username.");
            throw new IllegalArgumentException("Username must not be blank.");
        }

        URI uri = UriComponentsBuilder.fromHttpUrl(gitHubProperties.getBaseUrl())
            .path("/users/{username}/repos")
            .buildAndExpand(username)
            .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, "application/vnd.github+json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            return restTemplate
                .exchange(uri, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<GitHubRepo>>() {})
                .getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                log.error("User: {} not found.", username, e);
                throw new GitHubResourceNotFoundException(e);
            }

            log.error("Unable to list repos for user: {}", username, e);
            throw new GitHubClientException(e);
        } catch (HttpServerErrorException e) {
            log.error("Unable to list repos for user: {}", username, e);
            throw new GitHubServerException(e);
        }
    }
}
