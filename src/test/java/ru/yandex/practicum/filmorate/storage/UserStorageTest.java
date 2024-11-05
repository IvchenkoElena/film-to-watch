package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTest {
    UserDbStorage userStorage;

    @Test
    public void testFindUser() {
        assertThat(userStorage.getById(1))
                .isInstanceOf(User.class)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "Capitan")
                .hasFieldOrPropertyWithValue("email", "Capitan@yandex.ru");
    }

    @Test
    public void testGetUsers() {
        assertThat(userStorage.findAll()).isNotEmpty()
                .hasSize(3)
                .filteredOn("name", "Sparrow")
                .isNotEmpty()
                .hasExactlyElementsOfTypes(User.class);
    }

    @Test
    public void findFriends() {
        assertThat(userStorage.findFriends(1)).isNotEmpty()
                .hasSize(2)
                .isInstanceOf(Collection.class)
                .first()
                .extracting(User::getName)
                .isEqualTo("Jack");
    }

    @Test
    public void create() {
        User newUser = new User();
        newUser.setId(4);
        newUser.setEmail("WillTurner@yandex.ru");
        newUser.setName("Will Turner");
        newUser.setLogin("Will Turner");
        newUser.setBirthday(LocalDate.of(2000, 1, 1));

        assertThat(userStorage.save(newUser))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 4)
                .hasFieldOrPropertyWithValue("email", "WillTurner@yandex.ru");
    }

    @Test
    public void update() {
        User newUser = new User();
        newUser.setId(3);
        newUser.setEmail("TheSparrow@yandex.ru");
        newUser.setName("TheSparrow");
        newUser.setLogin("TheSparrow");
        newUser.setBirthday(LocalDate.of(2000, 1, 1));

        assertThat(userStorage.update(newUser))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 3)
                .hasFieldOrPropertyWithValue("name", "TheSparrow")
                .hasFieldOrPropertyWithValue("email", "TheSparrow@yandex.ru")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 1, 1));
    }

    @Test
    public void deleteUser() {
        userStorage.removeUser(1);

        assertThat(userStorage.findAll()).isNotEmpty()
                .hasSize(2)
                .filteredOn("name", "Capitan")
                .isEmpty();
    }
}