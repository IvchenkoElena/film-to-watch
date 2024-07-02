package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
    FilmService filmService;

    @BeforeEach
    void beforeEach() {
        filmService = new FilmService();
    }

    @Test
    void findAllNoFilms() {

        final List<Film> films = filmService.findAllFilms();

        assertTrue(films.isEmpty(), "Фильмов не должно быть");
    }

    @Test
    void findAllOneUser() {

        Film newFilm = new Film();
        String name = "testName";
        newFilm.setName(name);
        String description = "testDescription";
        newFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        newFilm.setReleaseDate(releaseDate);
        int duration = 300;
        newFilm.setDuration(duration);

        filmService.createFilm(newFilm);

        final List<Film> films = filmService.findAllFilms();

        assertNotNull(films, "Фильмы не возвращаются");
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
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        newFilm.setReleaseDate(releaseDate);
        int duration = 300;
        newFilm.setDuration(duration);

        filmService.createFilm(newFilm);

        int id = newFilm.getId();

        final List<Film> films = filmService.findAllFilms();

        assertEquals(1, films.size(), "Неверное количество фильмов");
        assertEquals(newFilm, films.getFirst(), "Фильмы не совпадают");
        assertEquals(name, films.getFirst().getName(), "Названия фильмов не совпадают");
        assertEquals(description, films.getFirst().getDescription(), "Описания фильмов не совпадают");
        assertEquals(releaseDate, films.getFirst().getReleaseDate(), "Даты релиза фильмов не совпадают");
        assertEquals(duration, films.getFirst().getDuration(), "Продолжительности фильмов не совпадают");
        assertEquals(id, films.getFirst().getId(), "Id фильмов не совпадают");
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
        Film newFilm1 = new Film();
        String name = "testName";
        newFilm1.setName(name);
        String description = "testDescription";
        newFilm1.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        newFilm1.setReleaseDate(releaseDate);
        int duration = 300;
        newFilm1.setDuration(duration);

        filmService.createFilm(newFilm1);

        int id1 = newFilm1.getId();

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

        assertEquals(id1 + 1, id2, "ID должен увеличиться на 1");
    }

    @Test
    void updateValidFilm() {
        Film oldFilm = new Film();
        String name = "testName";
        oldFilm.setName(name);
        String description = "testDescription";
        oldFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        oldFilm.setReleaseDate(releaseDate);
        int duration = 300;
        oldFilm.setDuration(duration);

        filmService.createFilm(oldFilm);

        int id = oldFilm.getId();

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
        Film oldFilm = new Film();
        String name = "testName";
        oldFilm.setName(name);
        String description = "testDescription";
        oldFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        oldFilm.setReleaseDate(releaseDate);
        int duration = 300;
        oldFilm.setDuration(duration);

        filmService.createFilm(oldFilm);


        Film newFilm = new Film();

        String newName = "testName";
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
    void updateFilmWrongId() {
        Film oldFilm = new Film();
        String name = "testName";
        oldFilm.setName(name);
        String description = "testDescription";
        oldFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        oldFilm.setReleaseDate(releaseDate);
        int duration = 300;
        oldFilm.setDuration(duration);

        filmService.createFilm(oldFilm);

        int id1 = oldFilm.getId();
        int id2 = id1 + 1;

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
        Film oldFilm = new Film();
        String name = "testName";
        oldFilm.setName(name);
        String description = "testDescription";
        oldFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        oldFilm.setReleaseDate(releaseDate);
        int duration = 300;
        oldFilm.setDuration(duration);

        filmService.createFilm(oldFilm);

        int id = oldFilm.getId();

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
        Film oldFilm = new Film();
        String name = "testName";
        oldFilm.setName(name);
        String description = "testDescription";
        oldFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        oldFilm.setReleaseDate(releaseDate);
        int duration = 300;
        oldFilm.setDuration(duration);

        filmService.createFilm(oldFilm);

        int id = oldFilm.getId();

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
        Film oldFilm = new Film();
        String name = "testName";
        oldFilm.setName(name);
        String description = "testDescription";
        oldFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        oldFilm.setReleaseDate(releaseDate);
        int duration = 300;
        oldFilm.setDuration(duration);

        filmService.createFilm(oldFilm);

        int id = oldFilm.getId();

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
        Film oldFilm = new Film();
        String name = "testName";
        oldFilm.setName(name);
        String description = "testDescription";
        oldFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        oldFilm.setReleaseDate(releaseDate);
        int duration = 300;
        oldFilm.setDuration(duration);

        filmService.createFilm(oldFilm);

        int id = oldFilm.getId();

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
        Film oldFilm = new Film();
        String name = "testName";
        oldFilm.setName(name);
        String description = "testDescription";
        oldFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        oldFilm.setReleaseDate(releaseDate);
        int duration = 300;
        oldFilm.setDuration(duration);

        filmService.createFilm(oldFilm);

        int id = oldFilm.getId();

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
        Film oldFilm = new Film();
        String name = "testName";
        oldFilm.setName(name);
        String description = "testDescription";
        oldFilm.setDescription(description);
        LocalDate releaseDate = LocalDate.of(1985, 5, 17);
        oldFilm.setReleaseDate(releaseDate);
        int duration = 300;
        oldFilm.setDuration(duration);

        filmService.createFilm(oldFilm);

        int id = oldFilm.getId();

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