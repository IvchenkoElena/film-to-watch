package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final DirectorStorage directorStorage;
    private final EventStorage eventStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("genreDbStorage") GenreStorage genreStorage,
                       @Qualifier("mpaDbStorage") MpaStorage mpaStorage,
                       @Qualifier("directorDbStorage") DirectorStorage directorStorage,
                       @Qualifier("eventDbStorage") EventStorage eventStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.directorStorage = directorStorage;
        this.eventStorage = eventStorage;
    }

    public List<Film> findAllFilms() {
        final List<Film> films = filmStorage.findAll();
        loadAdditionalFilmData(films);
        return films;
    }

    public Film findById(Integer filmId) {
        Film film = filmStorage.getById(filmId);
        loadAdditionalFilmData(List.of(film));
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

    public void removeFilm(Integer filmId) {
        filmStorage.removeFilm(filmId);
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
        if (newFilm.getDirectors() != null && !new HashSet<>(directorStorage.findAllDirectors().stream().map(Director::getId).toList()).containsAll(newFilm.getDirectors().stream().map(Director::getId).toList())) {
            String message = "Режиссер должен быть существующим";
            log.error(message);
            throw new ValidationException(message);
        }
    }

    public void addLike(Integer filmId, Integer userId) {
        // ищем фильм и пользователя с такими ID
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
        eventStorage.addEvent(new Event(userId, EventType.LIKE, EventOperation.ADD, filmId));
        Film film = filmStorage.getById(filmId);
        loadAdditionalFilmData(List.of(film));
        if (film.getLikes().contains(userId)) {
            String message = "Пользователь уже оценил этот фильм ранее";
            log.error(message);
            throw new AlreadyExistsException(message);
        }
        Integer likesCount = filmStorage.getById(filmId).getLikesCount() + 1;

        filmStorage.addLike(filmId, userId, likesCount);
    }

    public void removeLike(Integer filmId, Integer userId) {
        // ищем фильм и пользователя с такими ID
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
        Integer likesCount = filmStorage.getById(filmId).getLikesCount() - 1;
        filmStorage.removeLike(filmId, userId, likesCount);
        eventStorage.addEvent(new Event(userId, EventType.LIKE, EventOperation.REMOVE, filmId));
    }

    public List<Film> bestFilms(int count, Integer genreId, Integer year) {
        final List<Film> films = filmStorage.bestFilms(count, genreId, year);
        loadAdditionalFilmData(films);
        return films;
    }

    public List<Film> findFilmsByDirector(Integer directorId, String sortBy) {
        if (directorStorage.findDirectorById(directorId) == null) {
            String message = "Режиссер с id = " + directorId + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        final List<Film> films = filmStorage.findFilmsByDirector(directorId, sortBy);
        loadAdditionalFilmData(films);
        return films;
    }

    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        final List<Film> films = filmStorage.getCommonFilms(userId, friendId);
        loadAdditionalFilmData(films);
        return films;
    }

    public List<Film> searchFilms(String searchQuery, List<String> searchBy) {
        List<Film> foundFilms = searchFilmsByCriteria(searchQuery, searchBy);

        if (!foundFilms.isEmpty()) {
            loadAdditionalFilmData(foundFilms);
        }

        return foundFilms;
    }

    private List<Film> searchFilmsByCriteria(String searchQuery, List<String> searchBy) {
        Map<String, String> controlCriteria = new LinkedHashMap<>();
        controlCriteria.put("title", null);
        controlCriteria.put("director", null);

        searchBy.forEach(inputCriteria -> {
            if (!controlCriteria.containsKey(inputCriteria)) {
                throw new ValidationException("Задан неверный критерий поиска '%s'".formatted(inputCriteria));
            }

            controlCriteria.put(inputCriteria, searchQuery);
        });

        return filmStorage.searchFilms(controlCriteria.values().toArray(new String[0]));
    }

    private void loadAdditionalFilmData(List<Film> films) {
        genreStorage.loadGenres(films);
        directorStorage.loadDirectors(films);
        filmStorage.loadLikes(films);
    }
}
