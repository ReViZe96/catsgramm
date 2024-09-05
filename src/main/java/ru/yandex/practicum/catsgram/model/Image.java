package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;

@Data
@EqualsAndHashCode(of = "id")
public class Image {

    protected Long id;
    protected long postId;
    protected String originalFileName;
    protected File filePath;

}
