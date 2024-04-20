package io.github.alancs7.redditclone.exception;

public class RedditCloneException extends RuntimeException {

    public RedditCloneException(String message) {
        super(message);
    }

    public RedditCloneException(String message, Throwable cause) {
        super(message, cause);
    }

}
