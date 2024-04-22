package io.github.alancs7.redditclone.service;

import io.github.alancs7.redditclone.dto.SubredditDto;
import io.github.alancs7.redditclone.exception.ResourceNotFoundException;
import io.github.alancs7.redditclone.mapper.SubredditMapper;
import io.github.alancs7.redditclone.model.Subreddit;
import io.github.alancs7.redditclone.repository.SubredditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getId());

        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No subreddit found with ID: " + id));

        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
