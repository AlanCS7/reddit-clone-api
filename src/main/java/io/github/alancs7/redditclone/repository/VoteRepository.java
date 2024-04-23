package io.github.alancs7.redditclone.repository;

import io.github.alancs7.redditclone.model.Post;
import io.github.alancs7.redditclone.model.User;
import io.github.alancs7.redditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByIdDesc(Post post, User currentUser);
}