package io.github.alancs7.redditclone.mapper;

import io.github.alancs7.redditclone.dto.PostRequest;
import io.github.alancs7.redditclone.dto.PostResponse;
import io.github.alancs7.redditclone.model.Post;
import io.github.alancs7.redditclone.model.Subreddit;
import io.github.alancs7.redditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "id", source = "postRequest.id")
    @Mapping(target = "name", source = "postRequest.postName")
    @Mapping(target = "url", source = "postRequest.url")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "createdAt", ignore = true)
    Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "post.id")
    @Mapping(target = "postName", source = "post.name")
    @Mapping(target = "url", source = "post.url")
    @Mapping(target = "description", source = "post.description")
    @Mapping(target = "subredditName", source = "post.subreddit.name")
    @Mapping(target = "userName", source = "post.user.username")
    PostResponse mapToDto(Post post);

}
