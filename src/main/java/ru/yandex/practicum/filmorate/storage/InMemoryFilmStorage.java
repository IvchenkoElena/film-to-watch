package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film save(@RequestBody Film newFilm) {
        // формируем дополнительные данные
        newFilm.setId(getNextId());
        // сохраняем новый фильм в памяти приложения
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film update(@RequestBody Film newFilm) {
        Film oldFilm = films.get(newFilm.getId());
        // если публикация найдена и все условия соблюдены, обновляем её содержимое
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        return oldFilm;
    }

    @Override
    public Film getById(int id) {
        return films.get(id);
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
}
