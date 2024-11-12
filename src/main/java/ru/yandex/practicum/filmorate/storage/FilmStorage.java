package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> findAll();

    Film save(@RequestBody Film newFilm);

    Film getById(int id);

    Film update(@RequestBody Film newFilm);

    void removeFilm(Integer filmId);

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    List<Film> bestFilms(int count, Integer genreId, Integer year);

    List<Film> findFilmsByDirector(Integer directorId, String sortBy);

    List<Film> getCommonFilms(Integer userId, Integer friendId);

    List<Film> getRecommendedFilms(Integer userId);

    List<Film> searchFilms(String[] searchQueryByCriteria);

    void loadLikes(List<Film> films);
}
