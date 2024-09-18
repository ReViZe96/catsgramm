package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;
import java.util.*;

@Service
public class PostService {

    private final Map<Long, Post> posts = new HashMap<>();
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public List<Post> findAll(int from, int size, SortOrder sort) {
        List<Post> descSortedPost = posts.values().stream()
                .sorted(Post::compareTo)
                .skip(from)
                .limit(size)
                .toList();
        if (SortOrder.ASCENDING.equals(sort)) {
            List<Post> ascSortedPost = new ArrayList<>(descSortedPost);
            Collections.reverse(ascSortedPost);
            return ascSortedPost;
        } else {
            return descSortedPost;
        }
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        if (userService.findUserById(post.getAuthorId()).isEmpty()) {
            throw new NotFoundException("Автор поста не зарегистрирован в системе");
        }

        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {

        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }

            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    public Post getById(Long id) {
        Optional<Post> post = Optional.ofNullable(posts.get(id));
        if (post.isEmpty()) {
            throw new NotFoundException("Пост с id = " + id + " не найден");
        } else {
            return post.get();
        }
    }


    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
