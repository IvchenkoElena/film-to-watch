package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("genreDbStorage") GenreStorage genreStorage,
                       @Qualifier("mpaDbStorage") MpaStorage mpaStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    public List<Film> findAllFilms() {
        final List<Film> films = filmStorage.findAll();
        genreStorage.load(films); //тут использовала новый метод загрузки жанров
        return films;
    }

    public Film findById(Integer filmId) {
        Film film = filmStorage.getById(filmId);
        //film.setGenres(genreStorage.getGenresByFilmId(filmId)); // здесь оставила через отдельный запрос
        //или надо тут тоже как-то применить метод загрузки жанров, но только для одного фильма?
        genreStorage.load(List.of(film));
        return film;
    }

    public Film createFilm(Film newFilm) {
        // проверяем выполнение необходимых условий
        filmValidation(newFilm);
        // сохраняем новый фильм в памяти приложения
        return filmStorage.save(newFilm);
    }

    public Film updateFilm(Film newFilm) {
        filmValidation(newFilm);
        return filmStorage.update(newFilm);
    }

    //методы валидации

    private void filmValidation(Film newFilm) {
        if (newFilm.getName() == null || newFilm.getName().isBlank()) {
            String message = "Название не может быть пустым";
            log.error(message);
            throw new ValidationException(message);
        }
        if (newFilm.getDescription() == null || newFilm.getDescription().length() > 200) {
            String message = "Максимальная длина описания — 200 символов";
            log.error(message);
            throw new ValidationException(message);
        }
        if (newFilm.getReleaseDate() == null || newFilm.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            String message = "Дата релиза — не раньше 28 декабря 1895 года";
            log.error(message);
            throw new ValidationException(message);
        }
        if (newFilm.getDuration() == null || newFilm.getDuration() <= 0) {
            String message = "Продолжительность фильма должна быть положительным числом";
            log.error(message);
            throw new ValidationException(message);
        }
        if (newFilm.getMpa() == null || !mpaStorage.findAll().stream().map(Mpa::getId).toList().contains(newFilm.getMpa().getId())) {
            String message = "Рейтинг должен быть существующим";
            log.error(message);
            throw new ValidationException(message);
        }
        if (newFilm.getGenres() != null && !new HashSet<>(genreStorage.findAll().stream().map(Genre::getId).toList()).containsAll(newFilm.getGenres().stream().map(Genre::getId).toList())) {
            String message = "Жанр должен быть существующим";
            log.error(message);
            throw new ValidationException(message);
        }
    }

    public void addLike(Integer filmId, Integer userId) {
        // ищем пользователй с такими ID
        if (filmStorage.getById(filmId) == null) {
            String message = "Фильм с id = " + filmId + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        if (userStorage.getById(userId) == null) {
            String message = "Пользователь с id = " + userId + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        if (filmStorage.getById(filmId).getLikes().contains(userId)) {
            String message = "Пользователь уже оценил этот фильм ранее";
            log.error(message);
            throw new ValidationException(message);
        }
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        // ищем пользователй с такими ID
        if (filmStorage.getById(filmId) == null) {
            String message = "Фильм с id = " + filmId + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        if (userStorage.getById(userId) == null) {
            String message = "Пользователь с id = " + userId + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> bestFilms(int count) {
        final List<Film> films = filmStorage.bestFilms(count);
        genreStorage.load(films);//новый метод загрузки жанров
        return films;
    }
}