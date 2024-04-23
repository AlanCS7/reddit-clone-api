package io.github.alancs7.redditclone.exception;

public class SubredditNotFoundException extends RuntimeException {

    public SubredditNotFoundException(String message) {
        super(message);
    }

    public SubredditNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}