package io.github.alancs7.redditclone.service;

import io.github.alancs7.redditclone.exception.RedditCloneException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CommentServiceTest {

    @Test
    @DisplayName("Test should pass when comment do not contains swear words")
    void shouldNotContainsSwearWordsInsideComment() {
        CommentService commentService = new CommentService(null, null, null, null, null, null);
        assertThat(commentService.containsSwearWords("This is a clean comment")).isFalse();
    }

    @Test
    @DisplayName("Test should pass when comment contains swear words")
    void shouldContainsSwearWordsInsideComment() {
        CommentService commentService = new CommentService(null, null, null, null, null, null);

        assertThatThrownBy(() -> commentService.containsSwearWords("This is a shitty comment"))
                .isInstanceOf(RedditCloneException.class)
                .hasMessage("Comments contains unacceptable language");
    }
}