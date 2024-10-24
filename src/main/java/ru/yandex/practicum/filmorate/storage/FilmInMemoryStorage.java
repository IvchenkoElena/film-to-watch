package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmInMemoryStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getById(int id) {
        Film film = films.get(id);
        if (film == null) {
            String message = "Фильм с id = " + id + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        return film;
    }

    @Override
    public Film save(Film newFilm) {
        // формируем дополнительные данные
        newFilm.setId(getNextId());
        // сохраняем новый фильм в памяти приложения
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film update(Film newFilm) {
        Film oldFilm = films.get(newFilm.getId());
        if (oldFilm == null) {
            String message = "Фильм с id = " + newFilm.getId() + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        // если публикация найдена и все условия соблюдены, обновляем её содержимое
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        return oldFilm;
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

    public void addLike(Integer filmId, Integer userId) {
        getById(filmId).getLikes().add(userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        getById(filmId).getLikes().remove(userId);
    }

    public List<Film> bestFilms(int count) {
        return findAll().stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(count)
                .toList();
    }
}
