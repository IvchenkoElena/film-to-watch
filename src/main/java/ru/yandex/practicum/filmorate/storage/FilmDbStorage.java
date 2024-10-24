package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;


@Repository("filmDbStorage")
@Primary
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
//    private static final String FIND_BY_ID_QUERY = "SELECT * FROM FILMS f JOIN RATING r ON f.RATING_ID = r.RATING_ID WHERE f.FILM_ID = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private static final String UPDATE_QUERY = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_ID = ?" +
            "WHERE FILM_ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) VALUES(?, ?, ?, ?, ?)";
    private static final String INSERT_LIKE_QUERY = "INSERT INTO likes(film_id, user_id) VALUES(?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
    private static final String FIND_BEST_FILMS_QUERY = "SELECT f.* FROM FILMS f INNER JOIN (SELECT l.FILM_ID, COUNT(l.USER_ID)" +
            "AS LIKES_COUNT FROM LIKES l GROUP BY l.FILM_ID ORDER BY COUNT(l.USER_ID) DESC LIMIT ?)" +
            "AS flc ON f.FILM_ID = flc.FILM_ID ORDER BY flc.LIKES_COUNT DESC";
    private static final String INSERT_FILM_GENRE_QUERY = "INSERT INTO film_genre(FILM_ID, GENRE_ID) VALUES(?, ?)";
    //private static final String FIND_ALL_QUERY = "SELECT f.*, r.NAME AS RATING_NAME, r.DESCRIPTION AS RATING_DESCRIPTION FROM FILMS f JOIN RATING r ON f.RATING_ID = r.RATING_ID";
    //private static final String FIND_BY_ID_QUERY = "SELECT * FROM FILMS f JOIN RATING r ON f.RATING_ID = r.RATING_ID WHERE f.FILM_ID = ?";
    //не получилось заставить работать запрос с джойном фильмов и рейтингов


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
    public Film getById(int id){
        return findOne(FIND_BY_ID_QUERY, id)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм c ID %d не найден", id)));
    }

    @Override
    public Film update(@RequestBody Film newFilm){
        update(
                UPDATE_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().getId(),
                newFilm.getId()
        );
        List<Genre> genres = newFilm.getGenres();
        if (genres != null) {
            for (Genre genre : genres) {
                update(INSERT_FILM_GENRE_QUERY, newFilm.getId(), genre.getId());
            }
        }
        Set<Integer> likes = newFilm.getLikes();
        if (likes != null) {
            for (Integer userId : likes) {
                update(INSERT_LIKE_QUERY, newFilm.getId(), userId);
            }
        }
        return newFilm;
    }

    @Override
    public Film save(@RequestBody Film newFilm){
        Integer id = insert(
                INSERT_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().getId()
        );
        newFilm.setId(id);
        List<Genre> genres = newFilm.getGenres();
        if (genres != null) {
            for (Genre genre : genres) {
                update(INSERT_FILM_GENRE_QUERY, id, genre.getId());
            }
        }
        Set<Integer> likes = newFilm.getLikes();
        if (likes != null) {
            for (Integer userId : likes) {
                update(INSERT_LIKE_QUERY, id, userId);
            }
        }
        return newFilm;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        update(INSERT_LIKE_QUERY, filmId, userId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        delete(DELETE_LIKE_QUERY, filmId, userId);
    }

    @Override
    public List<Film> bestFilms(int count) {
        return findMany(FIND_BEST_FILMS_QUERY, count);
    }
}
