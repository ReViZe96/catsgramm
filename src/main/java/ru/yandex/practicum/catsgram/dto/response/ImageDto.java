package ru.yandex.practicum.catsgram.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ImageDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected Long id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected long postId;
    protected String originalFileName;
    protected String filePath;
}
