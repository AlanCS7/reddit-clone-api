package io.github.alancs7.redditclone.repository;

import io.github.alancs7.redditclone.model.Comment;
import io.github.alancs7.redditclone.model.Post;
import io.github.alancs7.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByPost(Post post);

    List<Comment> findCommentsByUser(User user);
}
