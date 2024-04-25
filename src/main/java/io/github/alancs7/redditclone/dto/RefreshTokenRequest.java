package io.github.alancs7.redditclone.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank String refreshToken,
                                  String username) {
}
