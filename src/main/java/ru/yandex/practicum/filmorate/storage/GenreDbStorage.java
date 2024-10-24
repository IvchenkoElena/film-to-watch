package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;


@Repository("genreDbStorage")
@Primary
public class GenreDbStorage extends BaseRepository<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM GENRES";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
    private static final String FIND_GENRES_BY_FILM_ID_QUERY = "SELECT g.* FROM GENRES g JOIN film_genre fg ON g.GENRE_ID = fg.GENRE_ID WHERE fg.FILM_ID = ?";

    // Инициализируем репозиторий
    @Autowired
    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Genre getById(int id) {
        return findOne(FIND_BY_ID_QUERY, id)
                .orElseThrow(() -> new NotFoundException(String.format("Жанр c ID %d не найден", id)));
    }

    public List<Genre> getGenresByFilmId(int id) {
        return findMany(FIND_GENRES_BY_FILM_ID_QUERY, id);
    }
}
