package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmService filmService;
    FilmController filmController;


    @BeforeEach
    void beforeEach() {
        filmService = new FilmService();
        filmController = new FilmController(filmService);
    }

    @Test
    void findAllNoFilms() {

        final List<Film> films = filmController.findAll();

        assertTrue(films.isEmpty(), "Фильмов не должно быть");
    }

    @Test
    void findAllOneFilm() {

        Film newFilm = new Film();
        String name = "testName";
        newFilm.setName(name);
        String description = "testDescription";
        newFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985,5,17);
        newFilm.setReleaseDate(releaseDate);
        int duration = 300;
        newFilm.setDuration(duration);

        filmController.create(newFilm);

        final List<Film> films = filmController.findAll();

        assertNotNull(films, "Фильмы не возврщаются");
        assertEquals(1, films.size(), "Неверное количество фильмов");
        assertEquals(newFilm, films.getFirst(), "Фильмы не совпадают");
    }

    @Test
    void createValidFilm() {

        Film newFilm = new Film();
        String name = "testName";
        newFilm.setName(name);
        String description = "testDescription";
        newFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985,5,17);
        newFilm.setReleaseDate(releaseDate);
        int duration = 300;
        newFilm.setDuration(duration);

        filmController.create(newFilm);

        int id = newFilm.getId();

        final List<Film> films = filmController.findAll();

        assertEquals(1, films.size(), "Неверное количество фильмов");
        assertEquals(newFilm, films.getFirst(), "Фильмы не совпадают");
        assertEquals(name, films.getFirst().getName(), "Названия фильмов не совпадают");
        assertEquals(description, films.getFirst().getDescription(), "Описания фильмов не совпадают");
        assertEquals(releaseDate, films.getFirst().getReleaseDate(), "Даты релиза фильмов не совпадают");
        assertEquals(duration, films.getFirst().getDuration(), "Продолжительности фильмов не совпадают");
        assertEquals(id, films.getFirst().getId(), "Id фильмов не совпадают");

    }

    @Test
    void updateValidFilm() {
        Film oldFilm = new Film();
        String name = "testName";
        oldFilm.setName(name);
        String description = "testDescription";
        oldFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985,5,17);
        oldFilm.setReleaseDate(releaseDate);
        int duration = 300;
        oldFilm.setDuration(duration);

        filmController.create(oldFilm);

        int id = oldFilm.getId();


        Film newFilm = new Film();
        newFilm.setId(id);
        String newName = "testName";
        newFilm.setName(newName);
        String newDescription = "testDescription";
        newFilm.setDescription(newDescription);
        LocalDate newReleaseDate = LocalDate.of(1985,5,17);
        newFilm.setReleaseDate(newReleaseDate);
        int newDuration = 300;
        newFilm.setDuration(newDuration);

        filmController.update(newFilm);

        final List<Film> films = filmController.findAll();

        assertEquals(newFilm, films.getFirst(), "Фильмы не совпадают");
        assertEquals(newName, films.getFirst().getName(), "Названия фильмов не совпадают");
        assertEquals(newDescription, films.getFirst().getDescription(), "Описания фильмов не совпадают");
        assertEquals(newReleaseDate, films.getFirst().getReleaseDate(), "Даты релиза фильмов не совпадают");
        assertEquals(newDuration, films.getFirst().getDuration(), "Продолжительности фильмов не совпадают");
        assertEquals(id, films.getFirst().getId(), "Id фильмов не совпадают");
    }
}