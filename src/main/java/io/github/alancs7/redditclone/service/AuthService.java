package io.github.alancs7.redditclone.service;

import io.github.alancs7.redditclone.dto.AuthenticationResponse;
import io.github.alancs7.redditclone.dto.LoginRequest;
import io.github.alancs7.redditclone.dto.RefreshTokenRequest;
import io.github.alancs7.redditclone.dto.RegisterRequest;
import io.github.alancs7.redditclone.exception.RedditCloneException;
import io.github.alancs7.redditclone.exception.ResourceNotFoundException;
import io.github.alancs7.redditclone.model.NotificationEmail;
import io.github.alancs7.redditclone.model.User;
import io.github.alancs7.redditclone.model.VerificationToken;
import io.github.alancs7.redditclone.repository.UserRepository;
import io.github.alancs7.redditclone.repository.VerificationTokenRepository;
import io.github.alancs7.redditclone.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public void signup(RegisterRequest registerRequest) {
        var user = new User();
        user.setEmail(registerRequest.email());
        user.setUsername(registerRequest.username());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setEnabled(false);

        userRepository.save(user);

        var token = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail(
                "Please activate your account",
                user.getEmail(),
                "http://localhost:8080/api/auth/accountVerification/" + token
        ));
    }

    public void verifyAccount(String token) {
        var verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RedditCloneException("Invalid token"));

        fetchUserAndEnable(verificationToken);
    }

    protected void fetchUserAndEnable(VerificationToken verificationToken) {
        var username = verificationToken.getUser().getUsername();

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + username));

        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        var authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = jwtProvider.generateToken(authenticate);

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.username())
                .build();
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        return userRepository.findByUsername(principal.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getSubject()));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.refreshToken());
        String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.username());

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.refreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.username())
                .build();
    }

    private String generateVerificationToken(User user) {
        var token = UUID.randomUUID().toString();

        var verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }
}
