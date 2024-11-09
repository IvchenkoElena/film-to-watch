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

    void addLike(Integer filmId, Integer userId, Integer likesCount);

    void removeLike(Integer filmId, Integer userId, Integer likesCount);

    List<Film> bestFilms(int count, Integer genreId, Integer year);

    List<Film> findFilmsByDirector(Integer directorId, String sortBy);

    List<Film> getCommonFilms(Integer userId, Integer friendId);
}
