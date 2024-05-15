package io.github.alancs7.redditclone.exception;

public class SubredditNotFoundException extends ResourceNotFoundException {

    public SubredditNotFoundException(String message) {
        super(message);
    }

}