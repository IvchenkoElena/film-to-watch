package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserService userService;

    @BeforeEach
    void beforeEach() {
        userService = new UserService();
    }

    @Test
    void findAllNoUsers() {

        final List<User> users = userService.findAllUsers();

        assertTrue(users.isEmpty(), "Пользователей не должно быть");
    }

    @Test
    void findAllOneUser() {

        User newUser = new User();
        String login = "testLogin";
        newUser.setLogin(login);
        String name = "testName";
        newUser.setName(name);
        String email = "email@mail.ru";
        newUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        newUser.setBirthday(birthday);

        userService.createUser(newUser);

        final List<User> users = userService.findAllUsers();

        assertNotNull(users, "Пользователи не возвращаются");
        assertEquals(1, users.size(), "Неверное количество пользователей");
        assertEquals(newUser, users.getFirst(), "Пользователи не совпадают");
    }

    @Test
    void createValidUser() {

        User newUser = new User();
        String login = "testLogin";
        newUser.setLogin(login);
        String name = "testName";
        newUser.setName(name);
        String email = "email@mail.ru";
        newUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        newUser.setBirthday(birthday);

        userService.createUser(newUser);

        int id = newUser.getId();

        final List<User> users = userService.findAllUsers();

        assertEquals(1, users.size(), "Неверное количество пользователей");
        assertEquals(newUser, users.getFirst(), "Пользователи не совпадают");
        assertEquals(name, users.getFirst().getName(), "Имена пользователей не совпадают");
        assertEquals(login, users.getFirst().getLogin(), "Логины пользователей не совпадают");
        assertEquals(email, users.getFirst().getEmail(), "Почты пользователей не совпадают");
        assertEquals(birthday, users.getFirst().getBirthday(), "Даты рождения пользователей не совпадают");
        assertEquals(id, users.getFirst().getId(), "Id пользователей не совпадают");

    }

    @Test
    void createUserNoName() {

        User newUser = new User();
        String login = "testLogin";
        newUser.setLogin(login);

        String email = "email@mail.ru";
        newUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        newUser.setBirthday(birthday);

        userService.createUser(newUser);

        final List<User> users = userService.findAllUsers();

        assertEquals(login, users.getFirst().getName(), "Имя должно быть эквивалентно логину");
        assertEquals(login, users.getFirst().getLogin(), "Логины пользователей не совпадают");
    }

    @Test
    void createUserNoEmail() {

        User newUser = new User();
        String login = "testLogin";
        newUser.setLogin(login);
        String name = "testName";
        newUser.setName(name);

        LocalDate birthday = LocalDate.of(1985, 5, 17);
        newUser.setBirthday(birthday);

        assertThrows(ValidationException.class, () -> userService.createUser(newUser));
    }

    @Test
    void createUserBlankEmail() {

        User newUser = new User();
        String login = "testLogin";
        newUser.setLogin(login);
        String name = "testName";
        newUser.setName(name);
        String email = " ";
        newUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        newUser.setBirthday(birthday);

        assertThrows(ValidationException.class, () -> userService.createUser(newUser));
    }

    @Test
    void createUserWrongEmail() {

        User newUser = new User();
        String login = "testLogin";
        newUser.setLogin(login);
        String name = "testName";
        newUser.setName(name);
        String email = "emailmail.ru";
        newUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        newUser.setBirthday(birthday);

        assertThrows(ValidationException.class, () -> userService.createUser(newUser));
    }

    @Test
    void createUserNullLogin() {

        User newUser = new User();


        String name = "testName";
        newUser.setName(name);
        String email = "email@mail.ru";
        newUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        newUser.setBirthday(birthday);

        assertThrows(ValidationException.class, () -> userService.createUser(newUser));
    }

    @Test
    void createUserBlankLogin() {

        User newUser = new User();
        String login = " ";
        newUser.setLogin(login);
        String name = "testName";
        newUser.setName(name);
        String email = "email@mail.ru";
        newUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        newUser.setBirthday(birthday);

        assertThrows(ValidationException.class, () -> userService.createUser(newUser));
    }

    @Test
    void createUserWrongLogin() {

        User newUser = new User();
        String login = "test Login";
        newUser.setLogin(login);
        String name = "testName";
        newUser.setName(name);
        String email = "email@mail.ru";
        newUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        newUser.setBirthday(birthday);

        assertThrows(ValidationException.class, () -> userService.createUser(newUser));
    }

    @Test
    void createUserBirthdayInFuture() {

        User newUser = new User();
        String login = "test Login";
        newUser.setLogin(login);
        String name = "testName";
        newUser.setName(name);
        String email = "email@mail.ru";
        newUser.setEmail(email);
        LocalDate birthday = LocalDate.of(2300, 5, 17);
        newUser.setBirthday(birthday);

        assertThrows(ValidationException.class, () -> userService.createUser(newUser));
    }

    @Test
    void generateId() {
        User newUser1 = new User();
        String newLogin = "testNewLogin";
        newUser1.setLogin(newLogin);
        String newName = "testNewName";
        newUser1.setName(newName);
        String newEmail = "newemail@mail.ru";
        newUser1.setEmail(newEmail);
        LocalDate newBirthday = LocalDate.of(1300, 5, 17);
        newUser1.setBirthday(newBirthday);

        userService.createUser(newUser1);

        int id1 = newUser1.getId();

        User newUser2 = new User();
        String newLogin2 = "testNewLogin2";
        newUser2.setLogin(newLogin2);
        String newName2 = "testNewName2";
        newUser2.setName(newName2);
        String newEmail2 = "newemail@mail.ru";
        newUser2.setEmail(newEmail2);
        LocalDate newBirthday2 = LocalDate.of(1300, 5, 17);
        newUser2.setBirthday(newBirthday2);

        userService.createUser(newUser2);

        int id2 = newUser2.getId();

        assertEquals(id1 + 1, id2, "ID должен увеличиться на 1");
    }

    @Test
    void updateValidUser() {
        User oldUser = new User();
        String login = "testLogin";
        oldUser.setLogin(login);
        String name = "testName";
        oldUser.setName(name);
        String email = "email@mail.ru";
        oldUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        oldUser.setBirthday(birthday);

        userService.createUser(oldUser);

        int id = oldUser.getId();

        User newUser = new User();
        newUser.setId(id);
        String newLogin = "testNewLogin";
        newUser.setLogin(newLogin);
        String newName = "testNewName";
        newUser.setName(newName);
        String newEmail = "newemail@mail.ru";
        newUser.setEmail(newEmail);
        LocalDate newBirthday = LocalDate.of(1975, 5, 17);
        newUser.setBirthday(newBirthday);

        userService.updateUser(newUser);

        final List<User> users = userService.findAllUsers();

        assertEquals(newUser, users.getFirst(), "Пользователи не совпадают");
        assertEquals(newName, users.getFirst().getName(), "Имена пользователей не совпадают");
        assertEquals(newLogin, users.getFirst().getLogin(), "Логины пользователей не совпадают");
        assertEquals(newEmail, users.getFirst().getEmail(), "Почты пользователей не совпадают");
        assertEquals(newBirthday, users.getFirst().getBirthday(), "Даты рождения пользователей не совпадают");
        assertEquals(id, users.getFirst().getId(), "Id пользователей не совпадают");
    }

    @Test
    void updateUserNullId() {
        User oldUser = new User();
        String login = "testLogin";
        oldUser.setLogin(login);
        String name = "testName";
        oldUser.setName(name);
        String email = "email@mail.ru";
        oldUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        oldUser.setBirthday(birthday);

        userService.createUser(oldUser);

        User newUser = new User();
        String newEmail = "newemail@mail.ru";
        newUser.setEmail(newEmail);
        String newLogin = "testNewLogin";
        newUser.setLogin(newLogin);
        String newName = "testNewName";
        newUser.setName(newName);

        LocalDate newBirthday = LocalDate.of(1975, 5, 17);
        newUser.setBirthday(newBirthday);

        assertThrows(ValidationException.class, () -> userService.updateUser(newUser));
    }

    @Test
    void updateUserWrongId() {
        User oldUser = new User();
        String login = "testLogin";
        oldUser.setLogin(login);
        String name = "testName";
        oldUser.setName(name);
        String email = "email@mail.ru";
        oldUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        oldUser.setBirthday(birthday);

        userService.createUser(oldUser);
        int id1 = oldUser.getId();
        int id2 = id1 + 1;

        User newUser = new User();
        newUser.setId(id2);
        String newEmail = "newemail@mail.ru";
        newUser.setEmail(newEmail);
        String newLogin = "testNewLogin";
        newUser.setLogin(newLogin);
        String newName = "testNewName";
        newUser.setName(newName);

        LocalDate newBirthday = LocalDate.of(1975, 5, 17);
        newUser.setBirthday(newBirthday);

        assertThrows(NotFoundException.class, () -> userService.updateUser(newUser));
    }

    @Test
    void updateUserNoName() {
        User oldUser = new User();
        String login = "testLogin";
        oldUser.setLogin(login);
        String name = "testName";
        oldUser.setName(name);
        String email = "email@mail.ru";
        oldUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        oldUser.setBirthday(birthday);

        userService.createUser(oldUser);

        int id = oldUser.getId();

        User newUser = new User();
        newUser.setId(id);
        String newLogin = "testNewLogin";
        newUser.setLogin(newLogin);

        String newEmail = "newemail@mail.ru";
        newUser.setEmail(newEmail);
        LocalDate newBirthday = LocalDate.of(1975, 5, 17);
        newUser.setBirthday(newBirthday);

        userService.updateUser(newUser);

        final List<User> users = userService.findAllUsers();

        assertEquals(newLogin, users.getFirst().getName(), "Имя должно быть эквивалентно логину");
        assertEquals(newLogin, users.getFirst().getLogin(), "Логины пользователей не совпадают");

    }

    @Test
    void updateUserNoEmail() {
        User oldUser = new User();
        String login = "testLogin";
        oldUser.setLogin(login);
        String name = "testName";
        oldUser.setName(name);
        String email = "email@mail.ru";
        oldUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        oldUser.setBirthday(birthday);

        userService.createUser(oldUser);

        int id = oldUser.getId();

        User newUser = new User();
        newUser.setId(id);
        String newLogin = "testNewLogin";
        newUser.setLogin(newLogin);
        String newName = "testNewName";
        newUser.setName(newName);

        LocalDate newBirthday = LocalDate.of(1975, 5, 17);
        newUser.setBirthday(newBirthday);

        assertThrows(ValidationException.class, () -> userService.updateUser(newUser));
    }

    @Test
    void updateUserBlankEmail() {
        User oldUser = new User();
        String login = "testLogin";
        oldUser.setLogin(login);
        String name = "testName";
        oldUser.setName(name);
        String email = "email@mail.ru";
        oldUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        oldUser.setBirthday(birthday);

        userService.createUser(oldUser);

        int id = oldUser.getId();

        User newUser = new User();
        newUser.setId(id);
        String newLogin = "testNewLogin";
        newUser.setLogin(newLogin);
        String newName = "testNewName";
        newUser.setName(newName);
        String newEmail = " ";
        newUser.setEmail(newEmail);
        LocalDate newBirthday = LocalDate.of(1975, 5, 17);
        newUser.setBirthday(newBirthday);

        assertThrows(ValidationException.class, () -> userService.updateUser(newUser));
    }

    @Test
    void updateUserWrongEmail() {
        User oldUser = new User();
        String login = "testLogin";
        oldUser.setLogin(login);
        String name = "testName";
        oldUser.setName(name);
        String email = "email@mail.ru";
        oldUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        oldUser.setBirthday(birthday);

        userService.createUser(oldUser);

        int id = oldUser.getId();

        User newUser = new User();
        newUser.setId(id);
        String newLogin = "testNewLogin";
        newUser.setLogin(newLogin);
        String newName = "testNewName";
        newUser.setName(newName);
        String newEmail = "newemailmail.ru";
        newUser.setEmail(newEmail);
        LocalDate newBirthday = LocalDate.of(1975, 5, 17);
        newUser.setBirthday(newBirthday);

        assertThrows(ValidationException.class, () -> userService.updateUser(newUser));
    }

    @Test
    void updateUserNullLogin() {
        User oldUser = new User();
        String login = "testLogin";
        oldUser.setLogin(login);
        String name = "testName";
        oldUser.setName(name);
        String email = "email@mail.ru";
        oldUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        oldUser.setBirthday(birthday);

        userService.createUser(oldUser);

        int id = oldUser.getId();

        User newUser = new User();
        newUser.setId(id);

        String newName = "testNewName";
        newUser.setName(newName);
        String newEmail = "newemail@mail.ru";
        newUser.setEmail(newEmail);
        LocalDate newBirthday = LocalDate.of(1975, 5, 17);
        newUser.setBirthday(newBirthday);

        assertThrows(ValidationException.class, () -> userService.updateUser(newUser));
    }


    @Test
    void updateUserBlankLogin() {
        User oldUser = new User();
        String login = "testLogin";
        oldUser.setLogin(login);
        String name = "testName";
        oldUser.setName(name);
        String email = "email@mail.ru";
        oldUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        oldUser.setBirthday(birthday);

        userService.createUser(oldUser);

        int id = oldUser.getId();

        User newUser = new User();
        newUser.setId(id);
        String newLogin = " ";
        newUser.setLogin(newLogin);
        String newName = "testNewName";
        newUser.setName(newName);
        String newEmail = "newemail@mail.ru";
        newUser.setEmail(newEmail);
        LocalDate newBirthday = LocalDate.of(1975, 5, 17);
        newUser.setBirthday(newBirthday);

        assertThrows(ValidationException.class, () -> userService.updateUser(newUser));
    }

    @Test
    void updateUserWrongLogin() {
        User oldUser = new User();
        String login = "testLogin";
        oldUser.setLogin(login);
        String name = "testName";
        oldUser.setName(name);
        String email = "email@mail.ru";
        oldUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        oldUser.setBirthday(birthday);

        userService.createUser(oldUser);

        int id = oldUser.getId();

        User newUser = new User();
        newUser.setId(id);
        String newLogin = "testNew Login";
        newUser.setLogin(newLogin);
        String newName = "testNewName";
        newUser.setName(newName);
        String newEmail = "newemail@mail.ru";
        newUser.setEmail(newEmail);
        LocalDate newBirthday = LocalDate.of(1975, 5, 17);
        newUser.setBirthday(newBirthday);

        assertThrows(ValidationException.class, () -> userService.updateUser(newUser));
    }

    @Test
    void updateUserBirthdayInFuture() {
        User oldUser = new User();
        String login = "testLogin";
        oldUser.setLogin(login);
        String name = "testName";
        oldUser.setName(name);
        String email = "email@mail.ru";
        oldUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985, 5, 17);
        oldUser.setBirthday(birthday);

        userService.createUser(oldUser);

        int id = oldUser.getId();

        User newUser = new User();
        newUser.setId(id);
        String newLogin = "testNewLogin";
        newUser.setLogin(newLogin);
        String newName = "testNewName";
        newUser.setName(newName);
        String newEmail = "newemail@mail.ru";
        newUser.setEmail(newEmail);
        LocalDate newBirthday = LocalDate.of(2300, 5, 17);
        newUser.setBirthday(newBirthday);

        assertThrows(ValidationException.class, () -> userService.updateUser(newUser));
    }


}