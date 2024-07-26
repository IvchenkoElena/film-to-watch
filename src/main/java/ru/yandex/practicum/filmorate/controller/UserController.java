package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;


import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        log.info("Получение списка пользователей");
        return userService.findAllUsers();
    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable Integer userId) {
        log.info("Получение пользователя с ID {}", userId);
        return userService.findById(userId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable Integer id) {
        log.info("Получение списка друзей пользователя с ID {}", id);
        return userService.findFriends(id);
    }

    @GetMapping("/{id}/friends/{friendId}")
    public List<User> findMutualFriends(@PathVariable Integer id,
                                        @PathVariable Integer friendId) {
        log.info("Получение списка общих друзей пользователей с ID {}, {}", id, friendId);
        return userService.findMutualFriends(id, friendId);
    }

    @PostMapping
    public User create(@RequestBody User newUser) {
        log.info("Создание нового пользователя: {}", newUser.toString());
        return userService.createUser(newUser);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Обновление пользователя с ID {}", newUser.getId());
        return userService.updateUser(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String addToFriends(@PathVariable Integer id,
                               @PathVariable Integer friendId) {
        log.info("Пользователи с ID {} и {} добавлены в друзья", id, friendId);
        userService.addToFriends(id, friendId);
        return "Пользователи с ID " + id + " и " + friendId + " теперь друзья";
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String removeFromFriends(@PathVariable Integer id,
                                    @PathVariable Integer friendId) {
        log.info("Пользователи с ID {} и {} удалены из друзей", id, friendId);
        userService.removeFromFriends(id, friendId);
        return "Пользователи с ID " + id + " и " + friendId + " больше не дружат";
    }
}
