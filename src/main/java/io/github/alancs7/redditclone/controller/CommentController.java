package io.github.alancs7.redditclone.controller;

import io.github.alancs7.redditclone.dto.CommentDto;
import io.github.alancs7.redditclone.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentDto commentDto) {
        commentService.createComment(commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(params = "postId")
    public ResponseEntity<List<CommentDto>> getCommentsForPost(@RequestParam("postId") Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    @GetMapping(params = "username")
    public ResponseEntity<List<CommentDto>> getCommentsByUser(@RequestParam("username") String username) {
        return ResponseEntity.ok(commentService.getCommentsByUser(username));
    }
}
