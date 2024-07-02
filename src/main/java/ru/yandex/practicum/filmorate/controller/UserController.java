package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;


import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService; //Надо ли делать конструктор?

    public UserController(UserService userService) {
        this.userService = userService; //Можно ли без него?
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAllUsers();
    }

    @PostMapping
    public User create(@RequestBody User newUser) {
        return userService.createUser(newUser);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        return userService.updateUser(newUser);
    }
}
