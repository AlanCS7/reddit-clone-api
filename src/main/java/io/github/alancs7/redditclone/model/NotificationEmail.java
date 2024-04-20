package io.github.alancs7.redditclone.model;

public record NotificationEmail(String subject,
                                String recipient,
                                String body) {
}
