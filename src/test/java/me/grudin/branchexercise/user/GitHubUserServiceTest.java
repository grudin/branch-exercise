package me.grudin.branchexercise.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import me.grudin.branchexercise.github.GitHubClient;
import me.grudin.branchexercise.github.GitHubGetUserResponse;
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
            )
        );

        var expected = new User(
            "foobar",
            "Foo Bar",
            "https://github.com/images/error/octocat_happy.gif",
            "Minneapolis",
            "foobar@example.com",
            "https://api.github.com/users/foobar",
            Instant.from(OffsetDateTime.of(2020, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC))
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
}
