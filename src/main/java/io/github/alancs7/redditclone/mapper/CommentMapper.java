package io.github.alancs7.redditclone.mapper;

import io.github.alancs7.redditclone.dto.CommentDto;
import io.github.alancs7.redditclone.model.Comment;
import io.github.alancs7.redditclone.model.Post;
import io.github.alancs7.redditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentDto.text")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", source = "user")
    Comment map(CommentDto commentDto, Post post, User user);

    @Mapping(target = "id", source = "comment.id")
    @Mapping(target = "text", source = "comment.text")
    @Mapping(target = "postId", source = "comment.post.id")
    @Mapping(target = "createdAt", source = "comment.createdAt")
    @Mapping(target = "userName", source = "comment.user.username")
    CommentDto mapToDto(Comment comment);
}
