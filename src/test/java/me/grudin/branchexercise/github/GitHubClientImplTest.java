package me.grudin.branchexercise.github;

import static org.junit.jupiter.api.Assertions.assertNull;

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
        @DisplayName("returns null for null username")
        void nullUsername() {
            assertNull(gitHubClient.getUser(null));
        }

        @Test
        @DisplayName("returns null for empty username")
        void emptyUsername() {
            assertNull(gitHubClient.getUser(""));
        }

        @Test
        @DisplayName("returns null for blank username")
        void blankUsername() {
            assertNull(gitHubClient.getUser("   "));
        }
    }
}
