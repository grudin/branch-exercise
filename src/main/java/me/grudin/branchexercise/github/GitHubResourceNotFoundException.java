package me.grudin.branchexercise.github;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GitHubResourceNotFoundException extends RuntimeException {

    public GitHubResourceNotFoundException() {
        super();
    }

    public GitHubResourceNotFoundException(String message) {
        super(message);
    }

    public GitHubResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitHubResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
