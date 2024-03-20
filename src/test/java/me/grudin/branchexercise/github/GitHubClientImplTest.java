package me.grudin.branchexercise.github;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class GitHubClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GitHubProperties gitHubProperties;

    private GitHubClientImpl gitHubClient;

    @BeforeEach
    void setup() {
        gitHubClient = new GitHubClientImpl(restTemplate, gitHubProperties);
    }

    @Nested
    @DisplayName("#getUser")
    class GetUser {

        @Test
        @DisplayName("throws `IllegalArgumentException` for null username")
        void nullUsername() {
            assertThrows(IllegalArgumentException.class, () -> gitHubClient.getUser(null));
        }

        @Test
        @DisplayName("throws `IllegalArgumentException` for empty username")
        void emptyUsername() {
            assertThrows(IllegalArgumentException.class, () -> gitHubClient.getUser(""));
        }

        @Test
        @DisplayName("throws `IllegalArgumentException` for blank username")
        void blankUsername() {
            assertThrows(IllegalArgumentException.class, () -> gitHubClient.getUser("   "));
        }
    }
}
