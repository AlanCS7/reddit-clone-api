package io.github.alancs7.redditclone.service;

import io.github.alancs7.redditclone.dto.RegisterRequest;
import io.github.alancs7.redditclone.exception.RedditCloneException;
import io.github.alancs7.redditclone.exception.ResourceNotFoundException;
import io.github.alancs7.redditclone.model.NotificationEmail;
import io.github.alancs7.redditclone.model.User;
import io.github.alancs7.redditclone.model.VerificationToken;
import io.github.alancs7.redditclone.repository.UserRepository;
import io.github.alancs7.redditclone.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    @Transactional
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

    private String generateVerificationToken(User user) {
        var token = UUID.randomUUID().toString();

        var verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    @Transactional
    public void verifyAccount(String token) {
        var verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RedditCloneException("Invalid token"));

        fetchUserAndEnable(verificationToken);
    }

    @Transactional
    protected void fetchUserAndEnable(VerificationToken verificationToken) {
        var username = verificationToken.getUser().getUsername();

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + username));

        user.setEnabled(true);
        userRepository.save(user);
    }
}
