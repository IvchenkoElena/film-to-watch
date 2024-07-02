package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class FilmService {
    private final Map<Integer, Film> films = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    public Collection<Film> findAllFilms() {
        return films.values();
    }

    public Film createFilm(@RequestBody Film newFilm) {
        // проверяем выполнение необходимых условий
        if (newFilm.getName() == null || newFilm.getName().isBlank()) {
            String message = "Название не может быть пустым";
            log.error(message);
            throw new ValidationException(message);
        }
        if (newFilm.getDescription().length() > 200) {
            String message = "Максимальная длина описания — 200 символов";
            log.error(message);
            throw new ValidationException(message);
        }
        if (newFilm.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            String message = "Дата релиза — не раньше 28 декабря 1895 года";
            log.error(message);
            throw new ValidationException(message);
        }
        if (newFilm.getDuration() <= 0) {
            String message = "Продолжительность фильма должна быть положительным числом";
            log.error(message);
            throw new ValidationException(message);
        }
        // формируем дополнительные данные
        newFilm.setId(getNextId());
        // сохраняем новый фильм в памяти приложения
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }


    // вспомогательный метод для генерации идентификатора нового фильма
    private Integer getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Film updateFilm(@RequestBody Film newFilm) {
        // проверяем необходимые условия
        if (newFilm.getId() == null) {
            String message = "Id должен быть указан";
            log.error(message);
            throw new ValidationException(message);
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            if (newFilm.getName() == null || newFilm.getName().isBlank()) {
                String message = "Название не может быть пустым";
                log.error(message);
                throw new ValidationException(message);
            }
            if (newFilm.getDescription().length() > 200) {
                String message = "Максимальная длина описания — 200 символов";
                log.error(message);
                throw new ValidationException(message);
            }
            if (newFilm.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
                String message = "Дата релиза — не раньше 28 декабря 1895 года";
                log.error(message);
                throw new ValidationException(message);
            }
            if (newFilm.getDuration() <= 0) {
                String message = "Продолжительность фильма должна быть положительным числом";
                log.error(message);
                throw new ValidationException(message);
            }
            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            oldFilm.setName(newFilm.getName());

            if (newFilm.getDescription() != null) {
                oldFilm.setDescription(newFilm.getDescription());
            }
            if (newFilm.getReleaseDate() != null) {
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
            }
            if (newFilm.getDuration() != null) {
                oldFilm.setDuration(newFilm.getDuration());
            }
            return oldFilm;
        }
        String message = "Пост с id = " + newFilm.getId() + " не найден";
        log.error(message);
        throw new NotFoundException(message);
    }

}
