package io.github.alancs7.redditclone.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    private String authenticationToken;
    private String refreshToken;
    private Instant expiresAt;
    private String username;
}
