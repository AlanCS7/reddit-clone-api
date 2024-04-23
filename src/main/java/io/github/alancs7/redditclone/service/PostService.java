package io.github.alancs7.redditclone.service;

import io.github.alancs7.redditclone.dto.PostRequest;
import io.github.alancs7.redditclone.dto.PostResponse;
import io.github.alancs7.redditclone.exception.PostNotFoundException;
import io.github.alancs7.redditclone.exception.ResourceNotFoundException;
import io.github.alancs7.redditclone.exception.SubredditNotFoundException;
import io.github.alancs7.redditclone.exception.UsernameNotFoundException;
import io.github.alancs7.redditclone.mapper.PostMapper;
import io.github.alancs7.redditclone.repository.PostRepository;
import io.github.alancs7.redditclone.repository.SubredditRepository;
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
public class PostService {

    private final SubredditRepository subredditRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    public void save(PostRequest postRequest) {
        var subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException("Subreddit with name " + postRequest.getSubredditName() + " not found"));

        postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("No Post found with ID: " + id));

        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        var subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new ResourceNotFoundException("No subreddit found with ID: " + subredditId));

        var posts = postRepository.findPostsBySubreddit(subreddit);

        return posts.stream().map(postMapper::mapToDto).toList();
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));

        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .toList();
    }
}
