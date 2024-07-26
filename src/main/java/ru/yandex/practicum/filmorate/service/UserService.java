package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> findAllUsers() {
        return userStorage.findAll();
    }

    public User findById(Integer userId) {
        User user = userStorage.getById(userId);
        if (user == null) {
            String message = "Пользователь с id = " + userId + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        return user;
    }

    public User createUser(@RequestBody User newUser) {
        // проверяем выполнение необходимых условий
        userValidation(newUser);
        return userStorage.save(newUser);
    }

    public User updateUser(@RequestBody User newUser) {
        // ищем пользователя с таким ID
        User oldUser = userStorage.getById(newUser.getId());
        if (oldUser == null) {
            String message = "Пользователь с id = " + newUser.getId() + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        // проверяем необходимые условия
        userValidation(newUser);
        // если публикация найдена и все условия соблюдены, обновляем её содержимое
        return userStorage.update(newUser);
    }

    //метод валидации
    private void userValidation(User newUser) {
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
        if (newUser.getBirthday() == null || newUser.getBirthday().isAfter(LocalDate.now())) {
            String message = "Дата рождения не может быть в будущем.";
            log.error(message);
            throw new ValidationException(message);
        }
    }

    public void addToFriends(Integer firstId, Integer secondId) {
        // ищем пользователй с такими ID
        if (userStorage.getById(firstId) == null) {
            String message = "Пользователь с id = " + firstId + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        if (userStorage.getById(secondId) == null) {
            String message = "Пользователь с id = " + secondId + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        if (userStorage.getById(firstId).getFriends().contains(secondId)) {
            String message = "Пользователи уже дружат";
            log.error(message);
            throw new ValidationException(message);
        }
        userStorage.getById(firstId).getFriends().add(secondId);
        userStorage.getById(secondId).getFriends().add(firstId);
    }

    public void removeFromFriends(Integer firstId, Integer secondId) {
        // ищем пользователй с такими ID
        if (userStorage.getById(firstId) == null) {
            String message = "Пользователь с id = " + firstId + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        if (userStorage.getById(secondId) == null) {
            String message = "Пользователь с id = " + secondId + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        if (!userStorage.getById(firstId).getFriends().contains(secondId)) {
            String message = "Пользователи не были друзьями";
            log.error(message);
            throw new ValidationException(message);
        }
        userStorage.getById(firstId).getFriends().remove(secondId);
        userStorage.getById(secondId).getFriends().remove(firstId);
    }

    public List<User> findFriends(Integer id) {
        // ищем пользователй с такими ID
        if (userStorage.getById(id) == null) {
            String message = "Пользователь с id = " + id + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        return userStorage.getById(id).getFriends().stream()
                .map(userStorage::getById)
                .toList();
    }

    public List<User> findMutualFriends(Integer firstId, Integer secondId) {
        // ищем пользователй с такими ID
        if (userStorage.getById(firstId) == null) {
            String message = "Пользователь с id = " + firstId + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        if (userStorage.getById(secondId) == null) {
            String message = "Пользователь с id = " + secondId + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        return userStorage.getById(firstId).getFriends().stream()
                .filter(id -> userStorage.getById(secondId).getFriends().contains(id))
                .map(userStorage::getById)
                .toList();
    }
}
