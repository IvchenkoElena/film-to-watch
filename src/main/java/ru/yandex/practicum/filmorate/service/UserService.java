package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final Map<Integer, User> users = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User createUser(User newUser) {
        // проверяем выполнение необходимых условий
        if (newUser.getEmail() == null || newUser.getEmail().isBlank() || !newUser.getEmail().contains("@")) {
            String message = "Электронная почта не может быть пустой и должна содержать символ @";
            log.error(message);
            throw new ValidationException(message);
        }
        if (newUser.getLogin() == null || newUser.getLogin().isBlank() || newUser.getLogin().contains(" ")) {
            String message = "Логин не может быть пустым и содержать пробелы";
            log.error(message);
            throw new ValidationException(message);
        }
        if (newUser.getName() == null) {
            newUser.setName(newUser.getLogin());
        }
        if (newUser.getBirthday().isAfter(LocalDate.now())) {
            String message = "Дата рождения не может быть в будущем.";
            log.error(message);
            throw new ValidationException(message);
        }
        // формируем дополнительные данные
        newUser.setId(getNextId());
        // сохраняем нового пользователя в памяти приложения
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private Integer getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public User updateUser(@RequestBody User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            String message = "Id должен быть указан";
            log.error(message);
            throw new ValidationException(message);
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getEmail() == null || newUser.getEmail().isBlank() || !newUser.getEmail().contains("@")) {
                String message = "Электронная почта не может быть пустой и должна содержать символ @";
                log.error(message);
                throw new ValidationException(message);
            }
            if (newUser.getLogin() == null || newUser.getLogin().isBlank() || newUser.getLogin().contains(" ")) {
                String message = "Логин не может быть пустым и содержать пробелы";
                log.error(message);
                throw new ValidationException(message);
            }
            if (newUser.getName() == null) {
                newUser.setName(newUser.getLogin());
            }
            if (newUser.getBirthday().isAfter(LocalDate.now())) {
                String message = "Дата рождения не может быть в будущем.";
                log.error(message);
                throw new ValidationException(message);
            }
            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            if (newUser.getName() != null) {
                oldUser.setName(newUser.getName());
            }
            if (newUser.getEmail() != null) {
                oldUser.setEmail(newUser.getEmail());
            }
            if (newUser.getLogin() != null) {
                oldUser.setLogin(newUser.getLogin());
            }
            if (newUser.getBirthday() != null) {
                oldUser.setBirthday(newUser.getBirthday());
            }
            return oldUser;
        }
        String message = "Пользователь с id = " + newUser.getId() + " не найден";
        log.error(message);
        throw new NotFoundException(message);
    }
}
