package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import java.util.Collection;


@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService; //Можно ли без конструктора обойтись?

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAllFilms();
    }

    @PostMapping
    public Film create(@RequestBody Film newFilm) {
        return filmService.createFilm(newFilm);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        return filmService.updateFilm(newFilm);
    }
}
