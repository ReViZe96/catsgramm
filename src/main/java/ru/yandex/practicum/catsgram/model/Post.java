package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(of = "id")
public class Post implements Comparable<Post> {

    protected Long id;
    protected long authorId;
    protected String description;
    protected Instant postDate;

    @Override
    public int compareTo(Post post) {
        if (this.postDate.isAfter(post.getPostDate())) {
            return -1;
        } else {
            return 1;
        }
    }

}
