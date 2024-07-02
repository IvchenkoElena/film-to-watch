package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserService userService;
    UserController userController;


    @BeforeEach
    void beforeEach() {
        userService = new UserService();
        userController = new UserController(userService);
    }

    @Test
    void findAllNoUsers() {

        final List<User> users = userController.findAll();

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
        LocalDate birthday = LocalDate.of(1985,5,17);
        newUser.setBirthday(birthday);

        userController.create(newUser);

        final List<User> users = userController.findAll();

        assertNotNull(users, "Пользователи не возврщаются");
        assertEquals(1, users.size(), "Неверное количество пользователей");
        assertEquals(newUser, users.getFirst(), "Пользователи не совпадают");
    }

    @Test
    void createUser() {

        User newUser = new User();
        String login = "testLogin";
        newUser.setLogin(login);
        String name = "testName";
        newUser.setName(name);
        String email = "email@mail.ru";
        newUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985,5,17);
        newUser.setBirthday(birthday);

        userService.createUser(newUser);

        int id = newUser.getId();

        final List<User> users = userController.findAll();

        assertEquals(1, users.size(), "Неверное количество пользователей");
        assertEquals(newUser, users.getFirst(), "Пользователи не совпадают");
        assertEquals(name, users.getFirst().getName(), "Имена пользователей не совпадают");
        assertEquals(login, users.getFirst().getLogin(), "Логины пользователей не совпадают");
        assertEquals(email, users.getFirst().getEmail(), "Почты пользователей не совпадают");
        assertEquals(birthday, users.getFirst().getBirthday(), "Даты рождения пользователей не совпадают");
        assertEquals(id, users.getFirst().getId(), "Id пользователей не совпадают");

    }

    @Test
    void updateUser() {
        User oldUser = new User();
        String login = "testLogin";
        oldUser.setLogin(login);
        String name = "testName";
        oldUser.setName(name);
        String email = "email@mail.ru";
        oldUser.setEmail(email);
        LocalDate birthday = LocalDate.of(1985,5,17);
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
        LocalDate newBirthday = LocalDate.of(1975,5,17);
        newUser.setBirthday(newBirthday);

        userService.updateUser(newUser);

        final List<User> users = userController.findAll();

        assertEquals(newUser, users.getFirst(), "Пользователи не совпадают");
        assertEquals(newName, users.getFirst().getName(), "Имена пользователей не совпадают");
        assertEquals(newLogin, users.getFirst().getLogin(), "Логины пользователей не совпадают");
        assertEquals(newEmail, users.getFirst().getEmail(), "Почты пользователей не совпадают");
        assertEquals(newBirthday, users.getFirst().getBirthday(), "Даты рождения пользователей не совпадают");
        assertEquals(id, users.getFirst().getId(), "Id пользователей не совпадают");
    }
}