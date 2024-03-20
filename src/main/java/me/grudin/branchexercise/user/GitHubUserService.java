package me.grudin.branchexercise.user;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import me.grudin.branchexercise.github.GitHubClient;
import me.grudin.branchexercise.github.GitHubGetUserResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GitHubUserService implements UserService {

    private final GitHubClient gitHubClient;

    @Override
    public User getUser(String username) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("Username must not be blank.");
        }

        return toDto(gitHubClient.getUser(username));
    }

    private User toDto(GitHubGetUserResponse response) {
        return new User(
            response.login(),
            response.name(),
            response.avatarUrl(),
            response.location(),
            response.email(),
            response.url(),
            response.createdAt()
        );
    }
}
