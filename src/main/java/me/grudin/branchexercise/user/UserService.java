package me.grudin.branchexercise.user;

interface UserService {
    /**
     * Retrieves a user.
     *
     * @param username the username, must not be blank
     * @return the user
     * @throws IllegalArgumentException if the username is blank
     */
    User getUser(String username);
}
