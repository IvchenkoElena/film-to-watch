package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> findAllFilms() {
        return filmStorage.getAll();
    }

    public Film findById(Integer filmId) {
        return filmStorage.getById(filmId);
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

    //метод валидации
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
        filmStorage.getById(filmId).getLikes().add(userId);
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
        if (!filmStorage.getById(filmId).getLikes().contains(userId)) {
            String message = "Пользователь еще не оценил этот фильм";
            log.error(message);
            throw new ValidationException(message);
        }
        filmStorage.getById(filmId).getLikes().remove(userId);
    }

    public List<Film> bestFilms(int count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(count)
                .toList();
    }
}