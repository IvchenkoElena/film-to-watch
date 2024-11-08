package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
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
    private static final String INSERT_FILM_DIRECTOR_QUERY = "INSERT INTO film_director(film_id, director_id) VALUES(?, ?)";
    private static final String DELETE_FILM_DIRECTOR_QUERY = "DELETE FROM film_director WHERE film_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT f.*, r.NAME AS RATING_NAME, r.DESCRIPTION AS RATING_DESCRIPTION, FROM FILMS f JOIN RATING r ON f.RATING_ID = r.RATING_ID";
    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + " WHERE f.FILM_ID = ?";
    private static final String FIND_BEST_FILMS_QUERY = FIND_ALL_QUERY + " ORDER BY f.likes_count DESC LIMIT ?";
    private static final String FIND_DIRECTOR_FILMS_SORTED_BY_YEARS_QUERY = FIND_ALL_QUERY + " inner join film_director fd on f.film_id = fd.film_id where fd.director_id = ? ORDER BY extract(year from f.release_date)";
    private static final String FIND_DIRECTOR_FILMS_SORTED_BY_LIKES_QUERY = FIND_ALL_QUERY + " inner join film_director fd on f.film_id = fd.film_id where fd.director_id = ? ORDER BY f.likes_count DESC";


    // Инициализируем репозиторий
    @Autowired
    public FilmDbStorage(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbcTemplate, RowMapper<Film> mapper) {
        super(jdbc, namedJdbcTemplate, mapper);
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
            saveGenres(newFilm);
        }
        delete(DELETE_FILM_DIRECTOR_QUERY, newFilm.getId());
        Set<Director> directors = newFilm.getDirectors();
        if (directors != null) {
            saveDirectors(newFilm);
        }
        Set<Integer> likes = newFilm.getLikes();
        if (likes != null) {
            saveLikes(newFilm);
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
            saveGenres(newFilm);
        }
        Set<Director> directors = newFilm.getDirectors();
        if (directors != null) {
            saveDirectors(newFilm);
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
    public List<Film> bestFilms(int count) {
        return findMany(FIND_BEST_FILMS_QUERY, count);
    }

    @Override
    public List<Film> findFilmsByDirector(Integer directorId, String sortBy) {
        String sql;
        if (sortBy.equals("year")) {
            sql = FIND_DIRECTOR_FILMS_SORTED_BY_YEARS_QUERY;
        } else if (sortBy.equals("likes")) {
            sql = FIND_DIRECTOR_FILMS_SORTED_BY_LIKES_QUERY;
        } else {
            throw new ValidationException("Некорректный параметр сортировки");
        }
        return findMany(sql, directorId);
    }

    private void saveGenres(Film film) {
        LinkedHashSet<Genre> genres = film.getGenres();
        jdbc.batchUpdate(INSERT_FILM_GENRE_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {

                Genre genre = genres.stream().toList().get(i);
                ps.setInt(1, film.getId());
                ps.setInt(2, genre.getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }

    private void saveDirectors(Film film) {
        Set<Director> directors = film.getDirectors();
        jdbc.batchUpdate(INSERT_FILM_DIRECTOR_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Director director = directors.stream().toList().get(i);
                ps.setInt(1, film.getId());
                ps.setInt(2, director.getId());
            }

            @Override
            public int getBatchSize() {
                return directors.size();
            }
        });
    }

    private void saveLikes(Film film) {
        Set<Integer> likes = film.getLikes();
        jdbc.batchUpdate(INSERT_LIKE_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Integer userId = likes.stream().toList().get(i);
                ps.setInt(1, film.getId());
                ps.setInt(2, userId);
            }

            @Override
            public int getBatchSize() {
                return likes.size();
            }
        });
    }

}