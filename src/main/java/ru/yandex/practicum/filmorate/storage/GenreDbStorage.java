package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;


@Repository("genreDbStorage")
@Primary
public class GenreDbStorage extends BaseRepository<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM GENRES";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
    private static final String FIND_FILM_GENRES_QUERY = "SELECT g.*, fg.FILM_ID from GENRES g, film_genre fg where fg.GENRE_ID = g.GENRE_ID AND fg.FILM_ID in ";

    // Инициализируем репозиторий
    @Autowired
    public GenreDbStorage(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbcTemplate, RowMapper<Genre> mapper) {
        super(jdbc, namedJdbcTemplate, mapper);
    }

    public List<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Genre getById(int id) {
        return findOne(FIND_BY_ID_QUERY, id)
                .orElseThrow(() -> new NotFoundException(String.format("Жанр c ID %d не найден", id)));
    }

    public void loadGenres(List<Film> films) {
        final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, film -> film));
        List<Integer> filmIds = films.stream().map(Film::getId).toList();
        SqlParameterSource parameters = new MapSqlParameterSource("filmIds", filmIds);

        namedJdbcTemplate.query(FIND_FILM_GENRES_QUERY + "(:filmIds)", parameters, (rs) -> {
            final Film film = filmById.get(rs.getInt("FILM_ID"));
            film.addGenre(mapper.mapRow(rs, 0));
        });
    }
}
