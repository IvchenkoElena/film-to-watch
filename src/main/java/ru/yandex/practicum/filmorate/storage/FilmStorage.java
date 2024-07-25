package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    public List<Film> getAll();

    public Film save(@RequestBody Film newFilm);

    public Film update(@RequestBody Film newFilm);

    public Film getById(long id);


}
