package ru.yandex.practicum.catsgram.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
public class UpdatePostRequest extends NewPostRequest {
    private String description;
    private Instant postDate;

    public boolean hasPostDate() {
        return postDate != null;
    }

}
