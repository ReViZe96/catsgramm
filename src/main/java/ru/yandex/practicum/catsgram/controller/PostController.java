package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.dto.request.NewPostRequest;
import ru.yandex.practicum.catsgram.dto.request.UpdatePostRequest;
import ru.yandex.practicum.catsgram.dto.response.PostDto;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.service.PostService;
import ru.yandex.practicum.catsgram.service.SortOrder;

import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostDto> getPosts(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "desc") String sort) {
        if (SortOrder.from(sort) == null) {
            throw new ParameterNotValidException("sort", "Переданное значение = " + sort +
                    " не соответствует допустимым (asc, ascending или desc, descending");
        }
        if (size <= 0) {
            throw new ParameterNotValidException("size", "Размер выборки должен быть больше нуля");
        }
        if (page < 0) {
            throw new ParameterNotValidException("page", "Значение начальной позиции выборки должно быть больше нуля");
        } else {
            return postService.getPosts(page, size, SortOrder.from(sort));
        }
    }

    @GetMapping("/{postId}")
    public PostDto getPostById(@PathVariable("postId") long postId) {
        return postService.getPostById(postId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@RequestBody NewPostRequest postRequest) {
        return postService.createPost(postRequest);
    }

    @PutMapping("/{postId}")
    public PostDto updatePost(@PathVariable("postId") long postId, @RequestBody UpdatePostRequest request) {
        return postService.updatePost(postId, request);
    }

}
