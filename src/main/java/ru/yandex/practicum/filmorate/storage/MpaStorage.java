package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    List<Mpa> findAll();

    Mpa getById(int id);

    Mpa findByFilmId(int filmId);
}
