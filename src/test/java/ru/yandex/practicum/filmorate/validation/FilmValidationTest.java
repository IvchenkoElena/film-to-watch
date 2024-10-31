package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
class FilmValidationTest {
    Film film;
    Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        film = new Film();
    }

    @Test
    void setFilmsWithCorrectData() {
        film.setId(1);
        film.setName("Название фильма");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2000, 5, 12));
        film.setDuration(120);
        film.setMpa(new Mpa(1, "G", "У фильма нет возрастных ограничений"));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(0, violations.size());
        assertTrue(violations.isEmpty(), "Ошибка валидации при заполнении класса Film корректными данными");
    }

    @Test
    void setFilmsWithEmptyData() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(5, violations.size());
        assertFalse(violations.isEmpty(), "Ошибка валидации при заполнении класса Film пустыми данными");
    }

    @Test
    void setFilmsWithIncorrectName() {
        film.setId(1);
        film.setName(" ");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2000, 5, 12));
        film.setDuration(120);
        film.setMpa(new Mpa(1, "G", "У фильма нет возрастных ограничений"));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setFilmsWithEmptyOnName() {
        film.setId(1);
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2000, 5, 12));
        film.setDuration(120);
        film.setMpa(new Mpa(1, "G", "У фильма нет возрастных ограничений"));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setFilmsWithEmptyDescription() {
        film.setId(1);
        film.setName("Название фильма");
        film.setReleaseDate(LocalDate.of(2000, 5, 12));
        film.setDuration(120);
        film.setMpa(new Mpa(1, "G", "У фильма нет возрастных ограничений"));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setFilmsWithDescriptionMoreThan200Symbols() {
        film.setId(1);
        film.setName("Название фильма");
        film.setDescription("Когда «Семёрка», «Расплата» и остальные команды супергероев объединяются для некоего "
                + "ежегодного масштабного мероприятия, Бутчер и компания следуют за ними. «Пацаны» нацелились на другую, "
                + "гораздо более крупную дичь, чем обычно, но когда раскрывается правда о великой американской трагедии, "
                + "охотники становятся жертвами.");
        film.setReleaseDate(LocalDate.of(2000, 5, 12));
        film.setDuration(120);
        film.setMpa(new Mpa(1, "G", "У фильма нет возрастных ограничений"));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setFilmsWithCorrectReleaseDate() {
        film.setId(1);
        film.setName("Название фильма");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        film.setDuration(120);
        film.setMpa(new Mpa(1, "G", "У фильма нет возрастных ограничений"));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(0, violations.size());
        assertTrue(violations.isEmpty(), "Ошибка валидации при заполнении даты релиза корректно");
    }

    @Test
    void setFilmsWithZeroDuration() {
        film.setId(1);
        film.setName("Название фильма");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2000, 5, 12));
        film.setDuration(0);
        film.setMpa(new Mpa(1, "G", "У фильма нет возрастных ограничений"));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setFilmsWithNegativeDuration() {
        film.setId(1);
        film.setName("Название фильма");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2000, 5, 12));
        film.setDuration(-120);
        film.setMpa(new Mpa(1, "G", "У фильма нет возрастных ограничений"));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }
}