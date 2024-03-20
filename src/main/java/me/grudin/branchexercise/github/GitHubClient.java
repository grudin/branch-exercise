package me.grudin.branchexercise.github;

interface GitHubClient {
    /**
     * Gets a user from GitHub.
     * See: <a href="https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-a-user">Get a User</a>
     *
     * @param username the username
     * @return the user
     */
    GitHubGetUserResponse getUser(String username);
}
