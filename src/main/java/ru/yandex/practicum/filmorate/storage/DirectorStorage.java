package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    List<Director> findAllDirectors();

    Director findDirectorById(Integer directorId);

    Director createDirector(Director newDirector);

    Director updateDirector(Director newDirector);

    void removeDirector(Integer directorId);
}
