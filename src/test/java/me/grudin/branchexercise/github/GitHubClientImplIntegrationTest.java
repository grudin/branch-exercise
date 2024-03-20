package me.grudin.branchexercise.github;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@WireMockTest(httpPort = 8123)
@ExtendWith(MockitoExtension.class)
class GitHubClientImplIntegrationTest {

    private final RestTemplate restTemplate = new RestTemplate();

    @Mock
    private GitHubProperties gitHubProperties;

    private GitHubClientImpl gitHubClient;

    @BeforeEach
    void setup() {
        when(gitHubProperties.getBaseUrl()).thenReturn("http://localhost:8123");

        gitHubClient = new GitHubClientImpl(restTemplate, gitHubProperties);
    }

    @Nested
    @DisplayName("#getUser")
    class GetUser {

        @Test
        @DisplayName("returns user")
        void getUser() {
            stubFor(
                get("/users/foobar").willReturn(
                    ok()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                            """
                            {
                              "login": "foobar",
                              "id": 1,
                              "node_id": "MDQ6VXNlcjE=",
                              "avatar_url": "https://github.com/images/error/octocat_happy.gif",
                              "gravatar_id": "",
                              "url": "https://api.github.com/users/foobar",
                              "html_url": "https://github.com/foobar",
                              "followers_url": "https://api.github.com/users/foobar/followers",
                              "following_url": "https://api.github.com/users/foobar/following{/other_user}",
                              "gists_url": "https://api.github.com/users/foobar/gists{/gist_id}",
                              "starred_url": "https://api.github.com/users/foobar/starred{/owner}{/repo}",
                              "subscriptions_url": "https://api.github.com/users/foobar/subscriptions",
                              "organizations_url": "https://api.github.com/users/foobar/orgs",
                              "repos_url": "https://api.github.com/users/foobar/repos",
                              "events_url": "https://api.github.com/users/foobar/events{/privacy}",
                              "received_events_url": "https://api.github.com/users/foobar/received_events",
                              "type": "User",
                              "site_admin": false,
                              "name": "Foo Bar",
                              "company": null,
                              "blog": "",
                              "location": "Minneapolis",
                              "email": "foobar@example.com",
                              "hireable": null,
                              "bio": null,
                              "twitter_username": null,
                              "public_repos": 1,
                              "public_gists": 0,
                              "followers": 0,
                              "following": 0,
                              "created_at": "2020-01-01T12:00:00Z",
                              "updated_at": "2024-01-01T12:00:00Z"
                            }
                            """
                        )
                )
            );

            assertEquals(
                new GitHubGetUserResponse(
                    "foobar",
                    1L,
                    "https://github.com/images/error/octocat_happy.gif",
                    "https://api.github.com/users/foobar",
                    "https://api.github.com/users/foobar/repos",
                    "Foo Bar",
                    "Minneapolis",
                    "foobar@example.com",
                    Instant.from(OffsetDateTime.of(2020, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC))
                ),
                gitHubClient.getUser("foobar")
            );
        }

        @Test
        @DisplayName("returns null for 404 Not Found response")
        void notFound() {
            stubFor(
                get("/users/foobar").willReturn(
                    WireMock.notFound()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(
                            """
                            {
                              "message": "Not Found",
                              "documentation_url": "https://docs.github.com/rest/users/users#get-a-user"
                            }
                            """
                        )
                )
            );

            assertNull(gitHubClient.getUser("foobar"));
        }

        @Test
        @DisplayName("returns null for 500 Internal Server Error")
        void internalServerError() {
            stubFor(get("/users/foobar").willReturn(WireMock.serverError()));

            assertNull(gitHubClient.getUser("foobar"));
        }
    }
}
