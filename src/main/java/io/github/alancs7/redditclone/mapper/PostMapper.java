package io.github.alancs7.redditclone.mapper;

import io.github.alancs7.redditclone.dto.PostRequest;
import io.github.alancs7.redditclone.dto.PostResponse;
import io.github.alancs7.redditclone.model.Post;
import io.github.alancs7.redditclone.model.Subreddit;
import io.github.alancs7.redditclone.model.User;
import io.github.alancs7.redditclone.repository.CommentRepository;
import io.github.alancs7.redditclone.util.TimeAgo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;

    @Mapping(target = "id", source = "postRequest.id")
    @Mapping(target = "name", source = "postRequest.postName")
    @Mapping(target = "url", source = "postRequest.url")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "createdAt", ignore = true)
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "post.id")
    @Mapping(target = "postName", source = "post.name")
    @Mapping(target = "url", source = "post.url")
    @Mapping(target = "description", source = "post.description")
    @Mapping(target = "subredditName", source = "post.subreddit.name")
    @Mapping(target = "userName", source = "post.user.username")
    @Mapping(target = "voteCount", source = "post.voteCount")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponse mapToDto(Post post);

    protected Integer commentCount(Post post) {
        return commentRepository.findCommentsByPost(post).size();
    }

    protected String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedAt().toInstant().toEpochMilli());
    }

}
