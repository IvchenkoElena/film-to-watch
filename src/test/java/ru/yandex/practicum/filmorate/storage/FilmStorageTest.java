package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmStorageTest {
    FilmDbStorage filmStorage;

    @Test
    public void testFindFilmById() {
        assertThat(filmStorage.getById(1))
                .isInstanceOf(Film.class)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "Тень");
    }

    @Test
    public void testFindAll() {
        assertThat(filmStorage.findAll())
                .isNotEmpty()
                .hasSize(4)
                .filteredOn("name", "Тень")
                .isNotEmpty()
                .hasExactlyElementsOfTypes(Film.class);
    }

    @Test
    public void testFindPopular() {
        final int count = 4;
        Collection<Film> films = filmStorage.bestFilms(count, null, null);

        assertThat(films).isNotEmpty()
                .hasSize(count)
                .isInstanceOf(Collection.class)
                .first()
                .extracting(Film::getId)
                .isEqualTo(2);
    }

    @Test
    public void testUpdateFilm() {
        Film newFilm = new Film();
        newFilm.setId(4);
        newFilm.setName("Гадкий я");
        newFilm.setDescription("Гадкий снаружи, но добрый внутри Грю намерен, тем не менее, ...");
        newFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        newFilm.setDuration(95);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        mpa.setName("G");
        mpa.setDescription("У фильма нет возрастных ограничений");
        newFilm.setMpa(mpa);

        assertThat(filmStorage.update(newFilm))
                .isNotNull()
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2000, 1, 1));
    }

    @Test
    public void testDeleteFilm() {
        filmStorage.removeFilm(1);

        assertThat(filmStorage.findAll())
                .isNotEmpty()
                .hasSize(3)
                .filteredOn("name", "Тень")
                .isEmpty();
    }
}