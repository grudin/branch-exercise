package me.grudin.branchexercise.github;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GitHubClientException extends RuntimeException {

    public GitHubClientException() {
        super();
    }

    public GitHubClientException(String message) {
        super(message);
    }

    public GitHubClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitHubClientException(Throwable cause) {
        super(cause);
    }
}
