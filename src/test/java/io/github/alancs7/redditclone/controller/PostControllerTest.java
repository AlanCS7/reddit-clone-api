package io.github.alancs7.redditclone.controller;

import io.github.alancs7.redditclone.dto.PostResponse;
import io.github.alancs7.redditclone.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = PostController.class)
public class PostControllerTest {

    @MockBean
    private PostService postService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should List All Posts When making GET request to endpoint - /api/posts/")
    @WithMockUser(username = "test")
    void shouldListPosts() throws Exception {
        PostResponse postResponse1 = new PostResponse(1L, "First Post", "www.google.com", "My first post", "First User", "First Subreddit", 0, 0, "1 hour ago", false, false);
        PostResponse postResponse2 = new PostResponse(2L, "Second Post", "www.youtube.com", "My second post", "Second User", "Second Subreddit", 0, 0, "2 hour ago", false, false);

        when(postService.getPosts()).thenReturn(List.of(postResponse1, postResponse2));

        mockMvc.perform(get("/api/posts"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].postName").value("First Post"))
                .andExpect(jsonPath("$.[0].url").value("www.google.com"))
                .andExpect(jsonPath("$.[1].id").value("2"))
                .andExpect(jsonPath("$.[1].postName").value("Second Post"))
                .andExpect(jsonPath("$.[1].url").value("www.youtube.com"));
    }
}