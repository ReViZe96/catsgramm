package ru.yandex.practicum.catsgram.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.dto.response.ImageDto;
import ru.yandex.practicum.catsgram.model.Image;

import java.nio.file.Path;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageMapper {

    public static Image mapToImage(Long postId, Path filePath, MultipartFile file) {
        Image image = new Image();
        image.setPostId(postId);
        // запоминаем название файла, которое было при его передаче
        image.setOriginalFileName(file.getOriginalFilename());
        image.setFilePath(filePath.toString());
        return image;
    }

    public static ImageDto mapToImageDto(Image image) {
        ImageDto dto = new ImageDto();
        dto.setId(image.getId());
        dto.setPostId(image.getPostId());
        dto.setOriginalFileName(image.getOriginalFileName());
        dto.setFilePath(image.getFilePath());
        return dto;
    }

}
