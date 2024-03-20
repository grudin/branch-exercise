package me.grudin.branchexercise.user;

import io.micrometer.common.util.StringUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.grudin.branchexercise.github.GitHubClient;
import me.grudin.branchexercise.github.GitHubRepo;
import me.grudin.branchexercise.github.GitHubUser;
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

        return toDto(gitHubClient.getUser(username), gitHubClient.getRepos(username));
    }

    private User toDto(GitHubUser gitHubUser, List<GitHubRepo> gitHubRepos) {
        return new User(
            gitHubUser.login(),
            gitHubUser.name(),
            gitHubUser.avatarUrl(),
            gitHubUser.location(),
            gitHubUser.email(),
            gitHubUser.url(),
            gitHubUser.createdAt(),
            gitHubRepos.stream().map(this::toDto).toList()
        );
    }

    private Repo toDto(GitHubRepo gitHubRepo) {
        return new Repo(gitHubRepo.name(), gitHubRepo.url());
    }
}
