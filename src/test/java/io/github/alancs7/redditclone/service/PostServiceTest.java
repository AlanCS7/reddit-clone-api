package io.github.alancs7.redditclone.service;

import io.github.alancs7.redditclone.dto.PostRequest;
import io.github.alancs7.redditclone.dto.PostResponse;
import io.github.alancs7.redditclone.mapper.PostMapper;
import io.github.alancs7.redditclone.model.Post;
import io.github.alancs7.redditclone.model.Subreddit;
import io.github.alancs7.redditclone.model.User;
import io.github.alancs7.redditclone.repository.PostRepository;
import io.github.alancs7.redditclone.repository.SubredditRepository;
import io.github.alancs7.redditclone.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private SubredditRepository subredditRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthService authService;

    @Mock
    private PostMapper postMapper;

    @Captor
    private ArgumentCaptor<Post> postArgumentCaptor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should pass when find post by id")
    void shouldFindPostById() {
        Post post = new Post(1L, "First Post", "www.google.com", "My first post", 0, null, OffsetDateTime.now(), null);
        PostResponse expectedPostResponse = new PostResponse(1L, "First Post", "www.google.com", "My first post", "Test User", "Test Subreddit", 0, 0, "1 hour ago", false, false);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postMapper.mapToDto(Mockito.any(Post.class))).thenReturn(expectedPostResponse);

        PostResponse actualPostResponse = postService.getPost(1L);

        assertThat(actualPostResponse.getId()).isEqualTo(expectedPostResponse.getId());
        assertThat(actualPostResponse.getPostName()).isEqualTo(expectedPostResponse.getPostName());
    }

    @Test
    @DisplayName("Should pass when save post")
    void shouldSavePost() {
        User currentUser = new User(1L, "Test User", "password", "user@email.com", OffsetDateTime.now(), true);
        Subreddit subreddit = new Subreddit(1L, "First subreddit", "Subreddit description", Collections.emptyList(), OffsetDateTime.now(), currentUser);
        Post post = new Post(1L, "First Post", "www.google.com", "My first post", 0, null, OffsetDateTime.now(), null);
        PostRequest postRequest = new PostRequest(null, "First subreddit", "First post", "www.google.com", "My first post");

        when(subredditRepository.findByName("First subreddit")).thenReturn(Optional.of(subreddit));
        when(postMapper.map(postRequest, subreddit, currentUser)).thenReturn(post);
        when(authService.getCurrentUser()).thenReturn(currentUser);

        postService.save(postRequest);

        verify(postRepository, Mockito.times(1)).save(postArgumentCaptor.capture());

        assertThat(postArgumentCaptor.getValue().getId()).isEqualTo(1L);
        assertThat(postArgumentCaptor.getValue().getName()).isEqualTo("First Post");
    }
}