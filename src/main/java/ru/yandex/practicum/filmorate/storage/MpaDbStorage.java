package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository("mpaDbStorage")
@Primary
public class MpaDbStorage extends BaseRepository<Mpa> implements MpaStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM RATING";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM RATING WHERE RATING_ID = ?";
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT r.* FROM RATING r JOIN FILMS f ON r.RATING_ID = f.RATING_ID WHERE f.FILM_ID = ?";

    // Инициализируем репозиторий
    @Autowired
    public MpaDbStorage(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbcTemplate, RowMapper<Mpa> mapper) {
        super(jdbc, namedJdbcTemplate, mapper);
    }

    public List<Mpa> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Mpa getById(int id) {
        return findOne(FIND_BY_ID_QUERY, id)
                .orElseThrow(() -> new NotFoundException(String.format("Рейтинг c ID %d не найден", id)));
    }

    public Mpa findByFilmId(int filmId) {
        return findOne(FIND_BY_FILM_ID_QUERY, filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Рейтинг фильма c ID %d не найден", filmId)));
    }
}
