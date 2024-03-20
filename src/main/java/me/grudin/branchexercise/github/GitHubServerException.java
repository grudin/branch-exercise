package me.grudin.branchexercise.github;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class GitHubServerException extends RuntimeException {

    public GitHubServerException() {
        super();
    }

    public GitHubServerException(String message) {
        super(message);
    }

    public GitHubServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitHubServerException(Throwable cause) {
        super(cause);
    }
}
