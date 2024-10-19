package ru.yandex.practicum.catsgram.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.catsgram.model.Image;

import java.util.List;
import java.util.Optional;

@Repository
public class ImageRepository extends BaseRepository<Image> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM image_storage";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM image_storage WHERE id = ?";
    private static final String FIND_BY_POST_ID_QUERY = "SELECT * FROM image_storage WHERE post_id =?";
    private static final String INSERT_QUERY = "INSERT INTO image_storage(original_name, file_path, post_id)" +
            "VALUES (?, ?, ?) returning id";

    public ImageRepository(JdbcTemplate jdbc, RowMapper<Image> mapper) {
        super(jdbc, mapper);
    }

    public List<Image> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Image> findById(long imageId) {
        return findOne(FIND_BY_ID_QUERY, imageId);
    }

    public List<Image> findByPostId(long postId) {
        return findMany(FIND_BY_POST_ID_QUERY, postId);
    }

    public Image save(Image image) {
        long id = insert(
                INSERT_QUERY,
                image.getOriginalFileName(),
                image.getFilePath(),
                image.getPostId()
        );
        image.setId(id);
        return image;
    }

}
