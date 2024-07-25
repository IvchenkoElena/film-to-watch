package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
    FilmService filmService;
    FilmStorage filmStorage;
    Film film;
    int id;

    @BeforeEach
    void beforeEach() {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage);

        film = new Film();
        String name = "testName";
        film.setName(name);
        String description = "testDescription";
        film.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        film.setReleaseDate(releaseDate);
        int duration = 300;
        film.setDuration(duration);

        filmService.createFilm(film);

        id = film.getId();
    }

    @Test
    void findAllOneUser() {
        final List<Film> films = filmService.findAllFilms();

        assertNotNull(films, "Фильмы не возвращаются");
        assertEquals(1, films.size(), "Неверное количество фильмов");
        assertEquals(film, films.getFirst(), "Фильмы не совпадают");
    }

    @Test
    void createValidFilm() {
        final List<Film> films = filmService.findAllFilms();

        assertEquals(1, films.size(), "Неверное количество фильмов");
        assertEquals(film, films.getFirst(), "Фильмы не совпадают");
        assertEquals(film.getName(), films.getFirst().getName(), "Названия фильмов не совпадают");
        assertEquals(film.getDescription(), films.getFirst().getDescription(), "Описания фильмов не совпадают");
        assertEquals(film.getReleaseDate(), films.getFirst().getReleaseDate(), "Даты релиза фильмов не совпадают");
        assertEquals(film.getDuration(), films.getFirst().getDuration(), "Продолжительности фильмов не совпадают");
        assertEquals(film.getId(), films.getFirst().getId(), "Id фильмов не совпадают");
    }

    @Test
    void createFilmNoName() {
        Film newFilm = new Film();

        String description = "testDescription";
        newFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        newFilm.setReleaseDate(releaseDate);
        int duration = 300;
        newFilm.setDuration(duration);

        assertThrows(ValidationException.class, () -> filmService.createFilm(newFilm));
    }


    @Test
    void createFilmBlankName() {
        Film newFilm = new Film();
        String name = " ";
        newFilm.setName(name);
        String description = "testDescription";
        newFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        newFilm.setReleaseDate(releaseDate);
        int duration = 300;
        newFilm.setDuration(duration);

        assertThrows(ValidationException.class, () -> filmService.createFilm(newFilm));
    }

    @Test
    void createFilmLongDescription() {
        Film newFilm = new Film();
        String name = "testName";
        newFilm.setName(name);
        String description = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation";
        newFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        newFilm.setReleaseDate(releaseDate);
        int duration = 300;
        newFilm.setDuration(duration);

        assertThrows(ValidationException.class, () -> filmService.createFilm(newFilm));
    }

    @Test
    void createFilmTooEarlyRelease() {
        Film newFilm = new Film();
        String name = "testName";
        newFilm.setName(name);
        String description = "testDescription";
        newFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1785, 5, 17);
        newFilm.setReleaseDate(releaseDate);
        int duration = 300;
        newFilm.setDuration(duration);

        assertThrows(ValidationException.class, () -> filmService.createFilm(newFilm));
    }

    @Test
    void createFilmWrongDuration() {
        Film newFilm = new Film();
        String name = "testName";
        newFilm.setName(name);
        String description = "testDescription";
        newFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        newFilm.setReleaseDate(releaseDate);
        int duration = -300;
        newFilm.setDuration(duration);

        assertThrows(ValidationException.class, () -> filmService.createFilm(newFilm));
    }

    @Test
    void createFilmZeroDuration() {
        Film newFilm = new Film();
        String name = "testName";
        newFilm.setName(name);
        String description = "testDescription";
        newFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        newFilm.setReleaseDate(releaseDate);
        int duration = 0;
        newFilm.setDuration(duration);

        assertThrows(ValidationException.class, () -> filmService.createFilm(newFilm));
    }

    @Test
    void generateId() {
        Film newFilm2 = new Film();
        String name2 = "testName2";
        newFilm2.setName(name2);
        String description2 = "testDescription2";
        newFilm2.setDescription(description2);
        LocalDate releaseDate2 = LocalDate.of(1985, 5, 17);
        newFilm2.setReleaseDate(releaseDate2);
        int duration2 = 300;
        newFilm2.setDuration(duration2);

        filmService.createFilm(newFilm2);

        int id2 = newFilm2.getId();

        assertEquals(id + 1, id2, "ID должен увеличиться на 1");
    }

    @Test
    void updateValidFilm() {
        Film newFilm = new Film();
        newFilm.setId(id);
        String newName = "testName";
        newFilm.setName(newName);
        String newDescription = "testDescription";
        newFilm.setDescription(newDescription);
        LocalDate newReleaseDate = LocalDate.of(1985, 5, 17);
        newFilm.setReleaseDate(newReleaseDate);
        int newDuration = 300;
        newFilm.setDuration(newDuration);

        filmService.updateFilm(newFilm);

        final List<Film> films = filmService.findAllFilms();

        assertEquals(newFilm, films.getFirst(), "Фильмы не совпадают");
        assertEquals(newName, films.getFirst().getName(), "Названия фильмов не совпадают");
        assertEquals(newDescription, films.getFirst().getDescription(), "Описания фильмов не совпадают");
        assertEquals(newReleaseDate, films.getFirst().getReleaseDate(), "Даты релиза фильмов не совпадают");
        assertEquals(newDuration, films.getFirst().getDuration(), "Продолжительности фильмов не совпадают");
        assertEquals(id, films.getFirst().getId(), "Id фильмов не совпадают");
    }

    @Test
    void updateFilmNullId() {
        Film newFilm = new Film();

        String newName = "testName";
        newFilm.setName(newName);
        String newDescription = "testDescription";
        newFilm.setDescription(newDescription);
        LocalDate newReleaseDate = LocalDate.of(1985, 5, 17);
        newFilm.setReleaseDate(newReleaseDate);
        int newDuration = 300;
        newFilm.setDuration(newDuration);

        assertThrows(NotFoundException.class, () -> filmService.updateFilm(newFilm));
    }

    @Test
    void updateFilmWrongId() {
        int id2 = id + 1;

        Film newFilm = new Film();
        newFilm.setId(id2);

        String newName = "testName";
        newFilm.setName(newName);
        String newDescription = "testDescription";
        newFilm.setDescription(newDescription);
        LocalDate newReleaseDate = LocalDate.of(1985, 5, 17);
        newFilm.setReleaseDate(newReleaseDate);
        int newDuration = 300;
        newFilm.setDuration(newDuration);

        assertThrows(NotFoundException.class, () -> filmService.updateFilm(newFilm));
    }

    @Test
    void updateFilmNoName() {
        Film newFilm = new Film();
        newFilm.setId(id);

        String newDescription = "testDescription";
        newFilm.setDescription(newDescription);
        LocalDate newReleaseDate = LocalDate.of(1985, 5, 17);
        newFilm.setReleaseDate(newReleaseDate);
        int newDuration = 300;
        newFilm.setDuration(newDuration);

        assertThrows(ValidationException.class, () -> filmService.updateFilm(newFilm));
    }

    @Test
    void updateFilmBlankName() {
        Film newFilm = new Film();
        newFilm.setId(id);
        String newName = " ";
        newFilm.setName(newName);
        String newDescription = "testDescription";
        newFilm.setDescription(newDescription);
        LocalDate newReleaseDate = LocalDate.of(1985, 5, 17);
        newFilm.setReleaseDate(newReleaseDate);
        int newDuration = 300;
        newFilm.setDuration(newDuration);

        assertThrows(ValidationException.class, () -> filmService.updateFilm(newFilm));
    }

    @Test
    void updateFilmLongDescription() {
        Film newFilm = new Film();
        newFilm.setId(id);
        String newName = "testName";
        newFilm.setName(newName);
        String newDescription = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation";
        newFilm.setDescription(newDescription);
        LocalDate newReleaseDate = LocalDate.of(1985, 5, 17);
        newFilm.setReleaseDate(newReleaseDate);
        int newDuration = 300;
        newFilm.setDuration(newDuration);

        assertThrows(ValidationException.class, () -> filmService.updateFilm(newFilm));
    }

    @Test
    void updateFilmTooEarlyRelease() {
        Film newFilm = new Film();
        newFilm.setId(id);

        String newName = "testName";
        newFilm.setName(newName);
        String newDescription = "testDescription";
        newFilm.setDescription(newDescription);
        LocalDate newReleaseDate = LocalDate.of(1785, 5, 17);
        newFilm.setReleaseDate(newReleaseDate);
        int newDuration = 300;
        newFilm.setDuration(newDuration);

        assertThrows(ValidationException.class, () -> filmService.updateFilm(newFilm));
    }

    @Test
    void updateFilmWrongDuration() {
        Film newFilm = new Film();
        newFilm.setId(id);

        String newName = "testName";
        newFilm.setName(newName);
        String newDescription = "testDescription";
        newFilm.setDescription(newDescription);
        LocalDate newReleaseDate = LocalDate.of(1785, 5, 17);
        newFilm.setReleaseDate(newReleaseDate);
        int newDuration = -300;
        newFilm.setDuration(newDuration);

        assertThrows(ValidationException.class, () -> filmService.updateFilm(newFilm));
    }

    @Test
    void updateFilmZeroDuration() {
        Film newFilm = new Film();
        newFilm.setId(id);

        String newName = "testName";
        newFilm.setName(newName);
        String newDescription = "testDescription";
        newFilm.setDescription(newDescription);
        LocalDate newReleaseDate = LocalDate.of(1785, 5, 17);
        newFilm.setReleaseDate(newReleaseDate);
        int newDuration = 0;
        newFilm.setDuration(newDuration);

        assertThrows(ValidationException.class, () -> filmService.updateFilm(newFilm));
    }
}