package io.github.alancs7.redditclone.dto;

import io.github.alancs7.redditclone.model.VoteType;

public record VoteDto(VoteType voteType,
                      Long postId) {
}
