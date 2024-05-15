package io.github.alancs7.redditclone.exception;

public class UsernameNotFoundException extends ResourceNotFoundException {

    public UsernameNotFoundException(String message) {
        super(message);
    }

}