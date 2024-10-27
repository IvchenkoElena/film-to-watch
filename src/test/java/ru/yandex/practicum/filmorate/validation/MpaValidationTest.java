package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
class MpaValidationTest {
    Mpa mpa;
    Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        mpa = new Mpa();
    }

    @Test
    void setMpaWithCorrectData() {
        mpa.setId(1);
        mpa.setName("G");
        mpa.setDescription("У фильма нет возрастных ограничений");

        Set<ConstraintViolation<Mpa>> violations = validator.validate(mpa);

        assertEquals(0, violations.size());
        assertTrue(violations.isEmpty(), "Ошибка валидации при заполнении класса Mpa");
    }

    @Test
    void setMpaWithEmptyData() {
        Set<ConstraintViolation<Mpa>> violations = validator.validate(mpa);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), "Ошибка валидации при заполнении класса Mpa пустыми данными");
    }

    @Test
    void setMpaWithEmptyName() {
        mpa.setId(1);
        mpa.setDescription("У фильма нет возрастных ограничений");

        Set<ConstraintViolation<Mpa>> violations = validator.validate(mpa);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }
}