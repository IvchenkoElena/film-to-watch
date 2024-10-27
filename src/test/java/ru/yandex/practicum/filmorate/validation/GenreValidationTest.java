package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class GenreValidationTest {
    Genre genre;
    Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        genre = new Genre();
    }

    @Test
    void setMpaWithCorrectData() {
        genre.setId(1);
        genre.setName("Комедия");

        Set<ConstraintViolation<Genre>> violations = validator.validate(genre);

        assertEquals(0, violations.size());
        assertTrue(violations.isEmpty(), "Ошибка валидации при заполнении класса Genre");
    }

    @Test
    void setMpaWithEmptyData() {
        Set<ConstraintViolation<Genre>> violations = validator.validate(genre);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), "Ошибка валидации при заполнении класса Genre пустыми данными");
    }
}