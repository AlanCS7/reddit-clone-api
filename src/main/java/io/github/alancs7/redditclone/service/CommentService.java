package io.github.alancs7.redditclone.service;

import io.github.alancs7.redditclone.dto.CommentDto;
import io.github.alancs7.redditclone.exception.PostNotFoundException;
import io.github.alancs7.redditclone.exception.UsernameNotFoundException;
import io.github.alancs7.redditclone.mapper.CommentMapper;
import io.github.alancs7.redditclone.model.NotificationEmail;
import io.github.alancs7.redditclone.model.User;
import io.github.alancs7.redditclone.repository.CommentRepository;
import io.github.alancs7.redditclone.repository.PostRepository;
import io.github.alancs7.redditclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private static final String POST_URL = "";

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final AuthService authService;
    private final MailService mailService;
    private final UserRepository userRepository;

    public void createComment(CommentDto commentDto) {
        var post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("No Post found with ID: " + commentDto.getPostId()));

        var comment = commentMapper.map(commentDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = post.getUser().getUsername() + " posted a comment on your post." + POST_URL;
        sendCommentNotification(message, post.getUser());
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByPost(Long postId) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("No Post found with ID: " + postId));

        return commentRepository.findCommentsByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByUser(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));

        return commentRepository.findCommentsByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .toList();
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(
                user.getUsername() + " Commented on your post",
                user.getEmail(),
                message
        ));
    }
}
