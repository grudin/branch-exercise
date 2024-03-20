package me.grudin.branchexercise.github;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    @Test
    @DisplayName("#getUser returns user")
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
    @DisplayName("#getUser throws `GitHubResourceNotFoundException` for 404 Not Found response")
    void getUser_UserNotFound() {
        stubFor(
            get("/users/foobar").willReturn(
                notFound()
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

        assertThrows(GitHubResourceNotFoundException.class, () -> gitHubClient.getUser("foobar"));
    }

    @Test
    @DisplayName("#getUser throws `GitHubClientException` for 429 Too Many Requests")
    void getUser_TooManyRequests() {
        stubFor(get("/users/foobar").willReturn(status(HttpStatus.TOO_MANY_REQUESTS.value())));

        assertThrows(GitHubClientException.class, () -> gitHubClient.getUser("foobar"));
    }

    @Test
    @DisplayName("#getUser throws `GitHubServerException` for 500 Internal Server Error")
    void getUser_InternalServerError() {
        stubFor(get("/users/foobar").willReturn(serverError()));

        assertThrows(GitHubServerException.class, () -> gitHubClient.getUser("foobar"));
    }

    @Test
    @DisplayName("#getRepos returns repos for user")
    void getRepos() {
        stubFor(
            get("/users/foobar/repos").willReturn(
                ok()
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBody(
                        """
                        [
                          {
                            "id": 1296269,
                            "node_id": "MDEwOlJlcG9zaXRvcnkxMjk2MjY5",
                            "name": "Hello-World",
                            "full_name": "foobar/Hello-World",
                            "owner": {
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
                              "site_admin": false
                            },
                            "private": false,
                            "html_url": "https://github.com/foobar/Hello-World",
                            "description": "This your first repo!",
                            "fork": false,
                            "url": "https://api.github.com/repos/foobar/Hello-World",
                            "archive_url": "https://api.github.com/repos/foobar/Hello-World/{archive_format}{/ref}",
                            "assignees_url": "https://api.github.com/repos/foobar/Hello-World/assignees{/user}",
                            "blobs_url": "https://api.github.com/repos/foobar/Hello-World/git/blobs{/sha}",
                            "branches_url": "https://api.github.com/repos/foobar/Hello-World/branches{/branch}",
                            "collaborators_url": "https://api.github.com/repos/foobar/Hello-World/collaborators{/collaborator}",
                            "comments_url": "https://api.github.com/repos/foobar/Hello-World/comments{/number}",
                            "commits_url": "https://api.github.com/repos/foobar/Hello-World/commits{/sha}",
                            "compare_url": "https://api.github.com/repos/foobar/Hello-World/compare/{base}...{head}",
                            "contents_url": "https://api.github.com/repos/foobar/Hello-World/contents/{+path}",
                            "contributors_url": "https://api.github.com/repos/foobar/Hello-World/contributors",
                            "deployments_url": "https://api.github.com/repos/foobar/Hello-World/deployments",
                            "downloads_url": "https://api.github.com/repos/foobar/Hello-World/downloads",
                            "events_url": "https://api.github.com/repos/foobar/Hello-World/events",
                            "forks_url": "https://api.github.com/repos/foobar/Hello-World/forks",
                            "git_commits_url": "https://api.github.com/repos/foobar/Hello-World/git/commits{/sha}",
                            "git_refs_url": "https://api.github.com/repos/foobar/Hello-World/git/refs{/sha}",
                            "git_tags_url": "https://api.github.com/repos/foobar/Hello-World/git/tags{/sha}",
                            "git_url": "git:github.com/foobar/Hello-World.git",
                            "issue_comment_url": "https://api.github.com/repos/foobar/Hello-World/issues/comments{/number}",
                            "issue_events_url": "https://api.github.com/repos/foobar/Hello-World/issues/events{/number}",
                            "issues_url": "https://api.github.com/repos/foobar/Hello-World/issues{/number}",
                            "keys_url": "https://api.github.com/repos/foobar/Hello-World/keys{/key_id}",
                            "labels_url": "https://api.github.com/repos/foobar/Hello-World/labels{/name}",
                            "languages_url": "https://api.github.com/repos/foobar/Hello-World/languages",
                            "merges_url": "https://api.github.com/repos/foobar/Hello-World/merges",
                            "milestones_url": "https://api.github.com/repos/foobar/Hello-World/milestones{/number}",
                            "notifications_url": "https://api.github.com/repos/foobar/Hello-World/notifications{?since,all,participating}",
                            "pulls_url": "https://api.github.com/repos/foobar/Hello-World/pulls{/number}",
                            "releases_url": "https://api.github.com/repos/foobar/Hello-World/releases{/id}",
                            "ssh_url": "git@github.com:foobar/Hello-World.git",
                            "stargazers_url": "https://api.github.com/repos/foobar/Hello-World/stargazers",
                            "statuses_url": "https://api.github.com/repos/foobar/Hello-World/statuses/{sha}",
                            "subscribers_url": "https://api.github.com/repos/foobar/Hello-World/subscribers",
                            "subscription_url": "https://api.github.com/repos/foobar/Hello-World/subscription",
                            "tags_url": "https://api.github.com/repos/foobar/Hello-World/tags",
                            "teams_url": "https://api.github.com/repos/foobar/Hello-World/teams",
                            "trees_url": "https://api.github.com/repos/foobar/Hello-World/git/trees{/sha}",
                            "clone_url": "https://github.com/foobar/Hello-World.git",
                            "mirror_url": "git:git.example.com/foobar/Hello-World",
                            "hooks_url": "https://api.github.com/repos/foobar/Hello-World/hooks",
                            "svn_url": "https://svn.github.com/foobar/Hello-World",
                            "homepage": "https://github.com",
                            "language": null,
                            "forks_count": 9,
                            "stargazers_count": 80,
                            "watchers_count": 80,
                            "size": 108,
                            "default_branch": "master",
                            "open_issues_count": 0,
                            "is_template": false,
                            "topics": [
                              "atom",
                              "electron",
                              "api"
                            ],
                            "has_issues": true,
                            "has_projects": true,
                            "has_wiki": true,
                            "has_pages": false,
                            "has_downloads": true,
                            "has_discussions": false,
                            "archived": false,
                            "disabled": false,
                            "visibility": "public",
                            "pushed_at": "2011-01-26T19:06:43Z",
                            "created_at": "2011-01-26T19:01:12Z",
                            "updated_at": "2011-01-26T19:14:43Z",
                            "permissions": {
                              "admin": false,
                              "push": false,
                              "pull": true
                            },
                            "security_and_analysis": {
                              "advanced_security": {
                                "status": "enabled"
                              },
                              "secret_scanning": {
                                "status": "enabled"
                              },
                              "secret_scanning_push_protection": {
                                "status": "disabled"
                              }
                            }
                          }
                        ]
                        """
                    )
            )
        );

        assertEquals(
            List.of(new GitHubRepo(1296269L, "Hello-World", "https://api.github.com/repos/foobar/Hello-World")),
            gitHubClient.getRepos("foobar")
        );
    }

    @Test
    @DisplayName("#getRepos returns empty list when user has no repos")
    void getRepos_NoReposForUser() {
        stubFor(
            get("/users/foobar/repos").willReturn(
                ok().withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).withBody("[]")
            )
        );

        assertEquals(List.of(), gitHubClient.getRepos("foobar"));
    }

    @Test
    @DisplayName("#getRepos throws `GitHubResourceNotFoundException` for 404 Not Found response")
    void getRepos_UserNotFound() {
        stubFor(
            get("/users/foobar/repos").willReturn(
                notFound()
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .withBody(
                        """
                        {
                          "message": "Not Found",
                          "documentation_url": "https://docs.github.com/rest/repos/repos#list-repositories-for-a-user"
                        }
                        """
                    )
            )
        );

        assertThrows(GitHubResourceNotFoundException.class, () -> gitHubClient.getRepos("foobar"));
    }

    @Test
    @DisplayName("#getRepos throws `GitHubClientException` for 429 Too Many Requests")
    void getRepos_TooManyRequests() {
        stubFor(get("/users/foobar/repos").willReturn(status(HttpStatus.TOO_MANY_REQUESTS.value())));

        assertThrows(GitHubClientException.class, () -> gitHubClient.getRepos("foobar"));
    }

    @Test
    @DisplayName("#getUser throws `GitHubServerException` for 500 Internal Server Error")
    void getRepos_InternalServerError() {
        stubFor(get("/users/foobar/repos").willReturn(serverError()));

        assertThrows(GitHubServerException.class, () -> gitHubClient.getRepos("foobar"));
    }
}
