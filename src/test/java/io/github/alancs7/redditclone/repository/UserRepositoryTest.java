package io.github.alancs7.redditclone.repository;

import io.github.alancs7.redditclone.BaseTest;
import io.github.alancs7.redditclone.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest extends BaseTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveUser() {
        User user = new User(null, "Test User", "password", "user@email.com", OffsetDateTime.now(), true);
        User userSaved = userRepository.save(user);

        assertThat(userSaved.getId()).isNotNull();
        assertThat(userSaved).usingRecursiveComparison().ignoringFields("id").isEqualTo(user);
    }

}