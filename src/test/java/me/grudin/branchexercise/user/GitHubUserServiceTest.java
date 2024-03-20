package me.grudin.branchexercise.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import me.grudin.branchexercise.github.GitHubClient;
import me.grudin.branchexercise.github.GitHubClientException;
import me.grudin.branchexercise.github.GitHubRepo;
import me.grudin.branchexercise.github.GitHubResourceNotFoundException;
import me.grudin.branchexercise.github.GitHubServerException;
import me.grudin.branchexercise.github.GitHubUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GitHubUserServiceTest {

    @Mock
    private GitHubClient gitHubClient;

    private GitHubUserService gitHubUserService;

    @BeforeEach
    void setup() {
        gitHubUserService = new GitHubUserService(gitHubClient);
    }

    @Test
    @DisplayName("returns user")
    void getUser() {
        when(gitHubClient.getUser("foobar")).thenReturn(
            new GitHubUser(
                "foobar",
                1L,
                "https://github.com/images/error/octocat_happy.gif",
                "https://api.github.com/users/foobar",
                "https://api.github.com/users/foobar/repos",
                "Foo Bar",
                "Minneapolis",
                "foobar@example.com",
                Instant.from(OffsetDateTime.of(2020, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC))
            )
        );

        when(gitHubClient.getRepos("foobar")).thenReturn(
            List.of(new GitHubRepo(1296269L, "Hello-World", "https://api.github.com/repos/foobar/Hello-World"))
        );

        var expected = new User(
            "foobar",
            "Foo Bar",
            "https://github.com/images/error/octocat_happy.gif",
            "Minneapolis",
            "foobar@example.com",
            "https://api.github.com/users/foobar",
            Instant.from(OffsetDateTime.of(2020, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC)),
            List.of(new Repo("Hello-World", "https://api.github.com/repos/foobar/Hello-World"))
        );

        assertEquals(expected, gitHubUserService.getUser("foobar"));
    }

    @Test
    @DisplayName("throws `IllegalArgumentException` for null username")
    void nullUsername() {
        assertThrows(IllegalArgumentException.class, () -> gitHubUserService.getUser(null));
    }

    @Test
    @DisplayName("throws `IllegalArgumentException` for empty username")
    void emptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> gitHubUserService.getUser(""));
    }

    @Test
    @DisplayName("throws `IllegalArgumentException` for blank username")
    void blankUsername() {
        assertThrows(IllegalArgumentException.class, () -> gitHubUserService.getUser("   "));
    }

    @Test
    @DisplayName("throws `GitHubResourceNotFoundException` when user is not found while getting user")
    void userNotFoundGetUser() {
        when(gitHubClient.getUser("foobar")).thenThrow(new GitHubResourceNotFoundException());

        assertThrows(GitHubResourceNotFoundException.class, () -> gitHubUserService.getUser("foobar"));
    }

    @Test
    @DisplayName("throws `GitHubResourceNotFoundException` when user is not found while getting repos")
    void userNotFoundGetRepos() {
        when(gitHubClient.getRepos("foobar")).thenThrow(new GitHubResourceNotFoundException());

        assertThrows(GitHubResourceNotFoundException.class, () -> gitHubUserService.getUser("foobar"));
    }

    @Test
    @DisplayName("throws `GitHubClientException` when user is not found while getting user")
    void gitHubClientExceptionGetUser() {
        when(gitHubClient.getUser("foobar")).thenThrow(new GitHubClientException());

        assertThrows(GitHubClientException.class, () -> gitHubUserService.getUser("foobar"));
    }

    @Test
    @DisplayName("throws `GitHubClientException` when user is not found while getting repos")
    void gitHubClientExceptionGetRepos() {
        when(gitHubClient.getRepos("foobar")).thenThrow(new GitHubClientException());

        assertThrows(GitHubClientException.class, () -> gitHubUserService.getUser("foobar"));
    }

    @Test
    @DisplayName("throws `GitHubServerException` when user is not found while getting user")
    void gitHubServerExceptionGetUser() {
        when(gitHubClient.getUser("foobar")).thenThrow(new GitHubServerException());

        assertThrows(GitHubServerException.class, () -> gitHubUserService.getUser("foobar"));
    }

    @Test
    @DisplayName("throws `GitHubServerException` when user is not found while getting repos")
    void gitHubServerExceptionGetRepos() {
        when(gitHubClient.getRepos("foobar")).thenThrow(new GitHubServerException());

        assertThrows(GitHubServerException.class, () -> gitHubUserService.getUser("foobar"));
    }
}
