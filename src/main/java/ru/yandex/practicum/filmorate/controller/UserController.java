package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;


import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получение списка пользователей");
        return userService.findAllUsers();
    }

    @PostMapping
    public User create(@RequestBody User newUser) {
        log.info("Создание нового пользователя");
        return userService.createUser(newUser);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Обновление пользователя с ID {}", newUser.getId());
        return userService.updateUser(newUser);
    }
}
