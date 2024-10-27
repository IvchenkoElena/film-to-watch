package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;


@Repository("genreDbStorage")
@Primary
public class GenreDbStorage extends BaseRepository<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM GENRES";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
    private static final String FIND_GENRES_BY_FILM_ID_QUERY = "SELECT g.* FROM GENRES g JOIN film_genre fg ON g.GENRE_ID = fg.GENRE_ID WHERE fg.FILM_ID = ?";
    private static final String FIND_FILM_GENRES_QUERY = "SELECT g.*, fg.FILM_ID from GENRES g, film_genre fg where fg.GENRE_ID = g.GENRE_ID AND fg.FILM_ID in ";

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

    public HashSet<Genre> getGenresByFilmId(int id) {
        return new HashSet<>(findMany(FIND_GENRES_BY_FILM_ID_QUERY, id));
    }

    public void load(List<Film> films) {
        final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, film -> film));
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));

        jdbc.query(FIND_FILM_GENRES_QUERY + "(" + inSql + ")", (rs) -> {
            final Film film = filmById.get(rs.getInt("FILM_ID"));
            film.addGenre(mapper.mapRow(rs, 0));
        }, films.stream().map(Film::getId).toArray());
    }
}
