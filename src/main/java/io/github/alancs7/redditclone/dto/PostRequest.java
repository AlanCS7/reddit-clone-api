package io.github.alancs7.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    private Long id;
    private String subredditName;
    private String postName;
    private String url;
    private String description;
}
