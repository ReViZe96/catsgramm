package ru.yandex.practicum.catsgram.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.catsgram.dto.request.NewPostRequest;
import ru.yandex.practicum.catsgram.dto.request.UpdatePostRequest;
import ru.yandex.practicum.catsgram.dto.response.PostDto;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostMapper {

    public static Post mapToPost(NewPostRequest request) {
        Post post = new Post();
        post.setAuthorId(request.getAuthorId());
        post.setDescription(request.getDescription());
        post.setPostDate(Instant.now());
        return post;
    }

    public static PostDto mapToPostDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setAuthorId(post.getAuthorId());
        dto.setDescription(post.getDescription());
        dto.setPostDate(post.getPostDate());
        return dto;
    }

    public static Post updatePostFields(Post post, UpdatePostRequest request) {
        post.setDescription(request.getDescription());
        if (request.hasPostDate()) {
            post.setPostDate(request.getPostDate());
        }
        return post;
    }

}
