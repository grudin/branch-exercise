package me.grudin.branchexercise.github;

public interface GitHubClient {
    /**
     * Gets a user from GitHub.
     * See: <a href="https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-a-user">Get a User</a>
     *
     * @param username the username, must not be blank
     * @return the user
     * @throws IllegalArgumentException        if username is blank
     * @throws GitHubResourceNotFoundException if user cannot be found
     * @throws GitHubClientException           if there is an unexpected client-side issue during the request
     * @throws GitHubServerException           if there is an unexpected server-side issue during the request
     */
    GitHubGetUserResponse getUser(String username);
}
