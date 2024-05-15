package io.github.alancs7.redditclone.exception;

public class PostNotFoundException extends ResourceNotFoundException {

    public PostNotFoundException(String message) {
        super(message);
    }

}
