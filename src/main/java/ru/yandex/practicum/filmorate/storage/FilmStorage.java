package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAll();

    Film save(@RequestBody Film newFilm);

    Film getById(int id);

    Film update(@RequestBody Film newFilm);
}
