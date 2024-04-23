package io.github.alancs7.redditclone.repository;

import io.github.alancs7.redditclone.model.Post;
import io.github.alancs7.redditclone.model.Subreddit;
import io.github.alancs7.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findPostsBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
