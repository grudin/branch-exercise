package me.grudin.branchexercise.user;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import me.grudin.branchexercise.github.GitHubClientException;
import me.grudin.branchexercise.github.GitHubResourceNotFoundException;
import me.grudin.branchexercise.github.GitHubServerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class)
class UserControllerMvcTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("returns 200 OK when user is found")
    void getUser() throws Exception {
        when(userService.getUser("foobar")).thenReturn(
            new User(
                "foobar",
                "Foo Bar",
                "https://github.com/images/error/octocat_happy.gif",
                "Minneapolis",
                "foobar@example.com",
                "https://api.github.com/users/foobar",
                Instant.from(OffsetDateTime.of(2020, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC))
            )
        );

        mockMvc
            .perform(get("/api/users/foobar"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(
                content()
                    .json(
                        """
                        {
                          "user_name": "foobar",
                          "display_name": "Foo Bar",
                          "avatar": "https://github.com/images/error/octocat_happy.gif",
                          "geo_location": "Minneapolis",
                          "email": "foobar@example.com",
                          "url": "https://api.github.com/users/foobar",
                          "created_at": "2020-01-01T12:00:00Z"
                        }
                        """
                    )
            );
    }

    @Test
    @DisplayName("returns 400 Bad Request when `IllegalArgumentException` is thrown")
    void illegalArgumentException() throws Exception {
        when(userService.getUser("foobar")).thenThrow(new IllegalArgumentException());

        mockMvc.perform(get("/api/users/foobar")).andExpect(status().isBadRequest()).andExpect(content().string(""));
    }

    @Test
    @DisplayName("returns 400 Bad Request when client-side error occurs while retrieving user")
    void clientException() throws Exception {
        when(userService.getUser("foobar")).thenThrow(new GitHubClientException());

        mockMvc.perform(get("/api/users/foobar")).andExpect(status().isBadRequest()).andExpect(content().string(""));
    }

    @Test
    @DisplayName("returns 404 Not Found when user is not found")
    void notFound() throws Exception {
        when(userService.getUser("foobar")).thenThrow(new GitHubResourceNotFoundException());

        mockMvc.perform(get("/api/users/foobar")).andExpect(status().isNotFound()).andExpect(content().string(""));
    }

    @Test
    @DisplayName("returns 502 Bad Gateway when server-side error occurs while retrieving user")
    void serverException() throws Exception {
        when(userService.getUser("foobar")).thenThrow(new GitHubServerException());

        mockMvc.perform(get("/api/users/foobar")).andExpect(status().isBadGateway()).andExpect(content().string(""));
    }
}
