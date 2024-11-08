package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public List<Director> findAllDirectors() {
        log.info("Получение списка режиссеров");
        return directorService.findAllDirectors();
    }

    @GetMapping("/{directorId}")
    public Director findDirectorById(@PathVariable Integer directorId) {
        log.info("Получение режиссера с ID {}", directorId);
        return directorService.findDirectorById(directorId);
    }

    @PostMapping
    public Director createDirector(@RequestBody Director newDirector) {
        log.info("Создание нового режиссера: {}", newDirector.toString());
        return directorService.createDirector(newDirector);
    }

    @PutMapping
    public Director updateDirector(@RequestBody Director newDirector) {
        log.info("Обновление режиссера с ID {}", newDirector.getId());
        return directorService.updateDirector(newDirector);
    }

    @DeleteMapping("/{directorId}")
    public void removeDirectorById(@PathVariable Integer directorId) {
        directorService.removeDirector(directorId);
        log.info("Удаление режиссера с ID {}", directorId);
    }
}
