package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;

import ru.yandex.practicum.filmorate.model.Mpa;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaStorageTest {
    MpaDbStorage mpaStorage;

    @Test
    public void testFindGenre() {
        assertThat(mpaStorage.findByFilmId(2))
                .isInstanceOf(Mpa.class)
                .hasFieldOrPropertyWithValue("id", 2)
                .hasFieldOrPropertyWithValue("name", "PG");
        ;
    }

    @Test
    public void findAll() {
        assertThat(mpaStorage.findAll()).isNotEmpty()
                .hasSize(5)
                .filteredOn("name", "PG-13")
                .isNotEmpty()
                .hasExactlyElementsOfTypes(Mpa.class);
    }
}