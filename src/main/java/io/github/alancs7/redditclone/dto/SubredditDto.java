package io.github.alancs7.redditclone.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubredditDto {

    private Long id;
    private String name;
    private String description;
    private int numberOfPosts;
}
