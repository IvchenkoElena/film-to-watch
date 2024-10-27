package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {

    @Autowired
    private FilmController filmController; //это поле серое и написано: Private field 'filmController' is never assigned

    @Test
    void createFilm() {
        Film newFilm = new Film();
        newFilm.setName("Film");
        newFilm.setDescription("film");
        newFilm.setReleaseDate(LocalDate.of(2020, 4, 1));
        newFilm.setDuration(120);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        mpa.setName("G");
        mpa.setDescription("У фильма нет возрастных ограничений");
        newFilm.setMpa(mpa);

        Film createdFilm = filmController.create(newFilm);
        assertNotNull(createdFilm.getId());
        assertEquals(newFilm.getName(), createdFilm.getName());
        assertEquals(newFilm.getDescription(), createdFilm.getDescription());
        assertEquals(newFilm.getReleaseDate(), createdFilm.getReleaseDate());
        assertEquals(newFilm.getDuration(), createdFilm.getDuration());
        assertEquals(newFilm.getMpa().getId(), createdFilm.getMpa().getId());
    }

    @Test
    void getFilmById() {
        Film newFilm = new Film();
        newFilm.setName("Film");
        newFilm.setDescription("film");
        newFilm.setReleaseDate(LocalDate.of(2020, 4, 1));
        newFilm.setDuration(120);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        mpa.setName("G");
        mpa.setDescription("У фильма нет возрастных ограничений");
        newFilm.setMpa(mpa);

        Film createdFilm = filmController.create(newFilm);
        Film retrievedFilm = filmController.findById(createdFilm.getId());
        assertEquals(createdFilm.getId(), retrievedFilm.getId());
        assertEquals(createdFilm.getName(), retrievedFilm.getName());
        assertEquals(createdFilm.getDescription(), retrievedFilm.getDescription());
        assertEquals(createdFilm.getReleaseDate(), retrievedFilm.getReleaseDate());
        assertEquals(createdFilm.getDuration(), retrievedFilm.getDuration());
        assertEquals(createdFilm.getMpa().getId(), retrievedFilm.getMpa().getId());
    }
}