package io.github.alancs7.redditclone.controller;

import io.github.alancs7.redditclone.dto.AuthenticationResponse;
import io.github.alancs7.redditclone.dto.LoginRequest;
import io.github.alancs7.redditclone.dto.RefreshTokenRequest;
import io.github.alancs7.redditclone.dto.RegisterRequest;
import io.github.alancs7.redditclone.service.AuthService;
import io.github.alancs7.redditclone.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        authService.signup(registerRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return ResponseEntity.ok("Account activated successfully");
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.refreshToken());
        return ResponseEntity.ok("Refresh token deleted successfully");
    }

}
