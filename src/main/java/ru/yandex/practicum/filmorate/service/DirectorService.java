package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public List<Director> findAllDirectors() {
        return directorStorage.findAllDirectors();
    }

    public Director findDirectorById(Integer directorId) {
        return directorStorage.findDirectorById(directorId);
    }

    public Director createDirector(Director newDirector) {
        return directorStorage.createDirector(newDirector);
    }

    public Director updateDirector(Director newDirector) {
        return directorStorage.updateDirector(newDirector);
    }

    public void removeDirector(Integer directorId) {
        directorStorage.removeDirector(directorId);
    }
}
