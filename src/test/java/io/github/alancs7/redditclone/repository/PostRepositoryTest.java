package io.github.alancs7.redditclone.repository;

import io.github.alancs7.redditclone.BaseTest;
import io.github.alancs7.redditclone.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest extends BaseTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void shouldSavePost() {
        Post expectedPost = new Post(null, "First post", "www.google.com", "Post description", 0, null, OffsetDateTime.now(), null);
        Post postSaved = postRepository.save(expectedPost);

        assertThat(postSaved.getId()).isNotNull();
        assertThat(postSaved).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedPost);
    }
}