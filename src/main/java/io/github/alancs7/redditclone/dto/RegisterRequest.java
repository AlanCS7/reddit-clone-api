package io.github.alancs7.redditclone.dto;

public record RegisterRequest(String email,
                              String username,
                              String password) {
}
