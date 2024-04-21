package io.github.alancs7.redditclone.repository;

import io.github.alancs7.redditclone.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit, Long> {

    @Query("FROM Subreddit s LEFT JOIN FETCH s.posts")
    List<Subreddit> findAll();
}