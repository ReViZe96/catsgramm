package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.dal.PostRepository;
import ru.yandex.practicum.catsgram.dal.UserRepository;
import ru.yandex.practicum.catsgram.dto.request.NewPostRequest;
import ru.yandex.practicum.catsgram.dto.request.UpdatePostRequest;
import ru.yandex.practicum.catsgram.dto.response.PostDto;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.mappers.PostMapper;
import ru.yandex.practicum.catsgram.model.Post;

import java.util.*;

@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    public List<PostDto> getPosts(int from, int size, SortOrder sort) {
        List<PostDto> descSortedPost = postRepository.findAll()
                .stream()
                .sorted(Post::compareTo)
                .skip(from)
                .limit(size)
                .map(PostMapper::mapToPostDto)
                .toList();
        if (SortOrder.ASCENDING.equals(sort)) {
            List<PostDto> ascSortedPost = new ArrayList<>(descSortedPost);
            Collections.reverse(ascSortedPost);
            return ascSortedPost;
        } else {
            return descSortedPost;
        }
    }

    public PostDto getPostById(long postId) {
        return postRepository.findById(postId)
                .map(PostMapper::mapToPostDto)
                .orElseThrow(() -> new NotFoundException("Пост с id = " + postId + " не найден"));
    }

    public PostDto createPost(NewPostRequest request) {
        isDescriptionNotEmpty(request);
        if (userRepository.findById(request.getAuthorId()).isEmpty()) {
            throw new NotFoundException("Автор поста не зарегистрирован в системе");
        }

        Post post = PostMapper.mapToPost(request);
        post = postRepository.save(post);
        return PostMapper.mapToPostDto(post);
    }

    public PostDto updatePost(long postId, UpdatePostRequest request) {
        isDescriptionNotEmpty(request);
        Post updatedPost = postRepository.findById(postId)
                .map(post -> PostMapper.updatePostFields(post, request))
                .orElseThrow(() -> new NotFoundException("Пост с id = " + postId + " не найден"));
        updatedPost = postRepository.update(updatedPost);
        return PostMapper.mapToPostDto(updatedPost);
    }


    private void isDescriptionNotEmpty(NewPostRequest request) {
        if (request.getDescription() == null || request.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
    }

}
