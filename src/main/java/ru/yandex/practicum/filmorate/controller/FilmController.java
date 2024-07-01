package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film newFilm) {
        // проверяем выполнение необходимых условий
        if (newFilm.getName() == null || newFilm.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (newFilm.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (newFilm.getReleaseDate().isBefore(Instant.ofEpochSecond(1895-12-28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (newFilm.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
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

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        // проверяем необходимые условия
        if (newFilm.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            if (newFilm.getName() == null || newFilm.getName().isBlank()) {
                throw new ValidationException("Название не может быть пустым");
            }
            if (newFilm.getDescription().length() > 200) {
                throw new ValidationException("Максимальная длина описания — 200 символов");
            }
            if (newFilm.getReleaseDate().isBefore(Instant.ofEpochSecond(1895-12-28))) {
                throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
            }
            if (newFilm.getDuration() <= 0) {
                throw new ValidationException("Продолжительность фильма должна быть положительным числом");
            }
            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            if (newFilm.getName() != null) {
                oldFilm.setName(newFilm.getName());
            }
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
        throw new ValidationException("Пост с id = " + newFilm.getId() + " не найден");
    }


}
