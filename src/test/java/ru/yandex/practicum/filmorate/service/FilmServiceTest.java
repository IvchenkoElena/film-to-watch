package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmServiceTest {

    @Autowired
    FilmService filmService;

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

        Film createdFilm = filmService.createFilm(newFilm);
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
        newFilm.setMpa(new Mpa(1, "G", "У фильма нет возрастных ограничений"));

        Film createdFilm = filmService.createFilm(newFilm);
        Film retrievedFilm = filmService.findById(createdFilm.getId());
        assertEquals(createdFilm.getId(), retrievedFilm.getId());
        assertEquals(createdFilm.getName(), retrievedFilm.getName());
        assertEquals(createdFilm.getDescription(), retrievedFilm.getDescription());
        assertEquals(createdFilm.getReleaseDate(), retrievedFilm.getReleaseDate());
        assertEquals(createdFilm.getDuration(), retrievedFilm.getDuration());
        assertEquals(createdFilm.getMpa().getId(), retrievedFilm.getMpa().getId());
    }
}