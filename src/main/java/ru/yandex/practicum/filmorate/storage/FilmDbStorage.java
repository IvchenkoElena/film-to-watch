package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


@Repository("filmDbStorage")
@Primary
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {

    private static final String UPDATE_FILM_QUERY = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_ID = ?, LIKES_COUNT = ?" +
            "WHERE FILM_ID = ?";
    private static final String UPDATE_LIKES_COUNT_QUERY = "UPDATE FILMS SET LIKES_COUNT = ? WHERE FILM_ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) VALUES(?, ?, ?, ?, ?)";
    private static final String DELETE_FILM_QUERY = "DELETE FROM films WHERE film_id = ?";
    private static final String INSERT_LIKE_QUERY = "INSERT INTO likes(film_id, user_id) VALUES(?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
    private static final String INSERT_FILM_GENRE_QUERY = "INSERT INTO film_genre(FILM_ID, GENRE_ID) VALUES(?, ?)";
    private static final String DELETE_FILM_GENRE_QUERY = "DELETE FROM film_genre WHERE FILM_ID = ?";
    private static final String FIND_ALL_QUERY = "SELECT f.*, r.NAME AS RATING_NAME, r.DESCRIPTION AS RATING_DESCRIPTION FROM FILMS f JOIN RATING r ON f.RATING_ID = r.RATING_ID";
    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + " WHERE f.FILM_ID = ?";
    private static final String FIND_BEST_FILMS_QUERY = FIND_ALL_QUERY + " ORDER BY f.likes_count DESC LIMIT ?";

    // Инициализируем репозиторий
    @Autowired
    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film getById(int id) {
        return findOne(FIND_BY_ID_QUERY, id)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм c ID %d не найден", id)));
    }

    @Override
    public Film update(@RequestBody Film newFilm) {
        update(
                UPDATE_FILM_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().getId(),
                newFilm.getLikes().size(),
                newFilm.getId()
        );
        delete(DELETE_FILM_GENRE_QUERY, newFilm.getId());
        LinkedHashSet<Genre> genres = newFilm.getGenres();
        if (genres != null) {
            jdbc.batchUpdate(INSERT_FILM_GENRE_QUERY, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Genre genre = genres.stream().toList().get(i);
                    ps.setInt(1, newFilm.getId());
                    ps.setInt(2, genre.getId());
                }

                @Override
                public int getBatchSize() {
                    return genres.size();
                }
            });
        }
        Set<Integer> likes = newFilm.getLikes();
        if (likes != null) {
            jdbc.batchUpdate(INSERT_LIKE_QUERY, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Integer userId = likes.stream().toList().get(i);
                    ps.setInt(1, newFilm.getId());
                    ps.setInt(2, userId);
                }

                @Override
                public int getBatchSize() {
                    return likes.size();
                }
            });
        }
        return newFilm;
    }

    @Override
    public Film save(@RequestBody Film newFilm) {
        Integer id = insert(
                INSERT_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().getId()
        );
        newFilm.setId(id);
        LinkedHashSet<Genre> genres = newFilm.getGenres();
        if (genres != null) {
            jdbc.batchUpdate(INSERT_FILM_GENRE_QUERY, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Genre genre = genres.stream().toList().get(i);
                    ps.setInt(1, id);
                    ps.setInt(2, genre.getId());
                }

                @Override
                public int getBatchSize() {
                    return genres.size();
                }
            });
        }
        return newFilm;
    }

    @Override
    public void removeFilm(Integer filmId) {
        boolean resultQuery = delete(DELETE_FILM_QUERY, filmId);
        if (!resultQuery) {
            throw new NotFoundException(String.format("Фильм с ID %d не найден", filmId));
        }
    }

    @Override
    public void addLike(Integer filmId, Integer userId, Integer likesCount) {
        update(INSERT_LIKE_QUERY, filmId, userId);
        update(UPDATE_LIKES_COUNT_QUERY, likesCount, filmId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId, Integer likesCount) {
        delete(DELETE_LIKE_QUERY, filmId, userId);
        update(UPDATE_LIKES_COUNT_QUERY, likesCount, filmId);
    }

    @Override
    public List<Film> bestFilms(int count, Integer genreId, Integer year) {
        String query = """
                    WITH prm AS (SELECT cast(? as integer) AS genre_id, cast(? as integer) AS p_year)
                    SELECT distinct f.*, r.NAME AS RATING_NAME, r.DESCRIPTION AS RATING_DESCRIPTION
                    FROM FILMS f
                    JOIN RATING r ON f.RATING_ID = r.RATING_ID
                    LEFT JOIN FILM_GENRE fg ON f.FILM_ID = fg.FILM_ID
                    JOIN prm on (FG.GENRE_ID = prm.genre_id OR prm.genre_id IS NULL) AND (EXTRACT(YEAR FROM release_date) = prm.p_year OR prm.p_year IS NULL)
                    ORDER BY f.likes_count DESC
                    LIMIT ?
                    """;
        return findMany(query, genreId, year, count);
    }

    @Override
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        String query = """
            WITH common AS (
                SELECT f.FILM_ID
                FROM FILMS f
                JOIN RATING r ON f.RATING_ID = r.RATING_ID
                JOIN LIKES l ON f.FILM_ID = l.FILM_ID
                WHERE l.USER_ID = ?
                INTERSECT
                SELECT f.FILM_ID
                FROM FILMS f
                JOIN RATING r ON f.RATING_ID = r.RATING_ID
                JOIN LIKES l ON f.FILM_ID = l.FILM_ID
                WHERE l.USER_ID = ?
            )
            SELECT f.*, r.NAME AS RATING_NAME, r.DESCRIPTION AS RATING_DESCRIPTION
            FROM common c
            JOIN FILMS f ON c.FILM_ID = f.FILM_ID
            JOIN RATING r ON f.RATING_ID = r.RATING_ID
            ORDER BY (SELECT COUNT(*) FROM LIKES WHERE FILM_ID = f.FILM_ID) DESC
            """;
        return findMany(query, userId, friendId);
    }

}