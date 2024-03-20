package me.grudin.branchexercise.github;

import java.util.List;

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
    GitHubUser getUser(String username);

    /**
     * List repos for a GitHub user.
     * See: <a href="https://docs.github.com/en/rest/repos/repos?apiVersion=2022-11-28#list-repositories-for-a-user">List repositories for a user</a>
     *
     * @param username the username, must not be blank
     * @return a list of repos for the user
     * @throws IllegalArgumentException        if username is blank
     * @throws GitHubResourceNotFoundException if user cannot be found
     * @throws GitHubClientException           if there is an unexpected client-side issue during the request
     * @throws GitHubServerException           if there is an unexpected server-side issue during the request
     */
    List<GitHubRepo> getRepos(String username);
}
