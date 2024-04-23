package io.github.alancs7.redditclone.service;

import io.github.alancs7.redditclone.dto.VoteDto;
import io.github.alancs7.redditclone.exception.PostNotFoundException;
import io.github.alancs7.redditclone.exception.RedditCloneException;
import io.github.alancs7.redditclone.model.Post;
import io.github.alancs7.redditclone.model.Vote;
import io.github.alancs7.redditclone.model.VoteType;
import io.github.alancs7.redditclone.repository.PostRepository;
import io.github.alancs7.redditclone.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        var post = postRepository.findById(voteDto.postId())
                .orElseThrow(() -> new PostNotFoundException("No Post found with ID: " + voteDto.postId()));

        validateVoteType(voteDto, post);

        int voteCount = post.getVoteCount() + (VoteType.UPVOTE.equals(voteDto.voteType()) ? 1 : -1);
        post.setVoteCount(voteCount);

        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private void validateVoteType(VoteDto voteDto, Post post) {
        voteRepository.findTopByPostAndUserOrderByIdDesc(post, authService.getCurrentUser())
                .ifPresent(vote -> {
                    if (vote.getVoteType().equals(voteDto.voteType())) {
                        throw new RedditCloneException("You have already " + voteDto.voteType() + "'d for this post");
                    }
                });
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.voteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
