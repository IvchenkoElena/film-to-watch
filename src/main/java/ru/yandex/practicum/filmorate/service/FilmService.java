package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FilmService {
    private final Map<Integer, Film> films = new HashMap<>();

    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    public Film createFilm(@RequestBody Film newFilm) {
        // проверяем выполнение необходимых условий
        filmValidation(newFilm);
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

    public Film updateFilm(@RequestBody Film newFilm) {
        // ищем фильм с таким Id
        Film oldFilm = films.get(newFilm.getId());
        if (oldFilm  == null) {
            String message = "Пост с id = " + newFilm.getId() + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }

        // проверяем необходимые условия
        filmValidation(newFilm);

        // если публикация найдена и все условия соблюдены, обновляем её содержимое
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());

        return oldFilm;
    }
}