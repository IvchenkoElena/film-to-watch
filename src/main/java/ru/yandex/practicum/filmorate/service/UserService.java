package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAllUsers() {
        return userStorage.findAll();
    }

    public User findById(Integer userId) {
        return userStorage.getById(userId);
    }

    public User createUser(User newUser) {
        // проверяем выполнение необходимых условий
        userValidation(newUser);
        return userStorage.save(newUser);
    }

    public User updateUser(User newUser) {
        userValidation(newUser);
        // если публикация найдена и все условия соблюдены, обновляем её содержимое
        return userStorage.update(newUser);
    }

    public void removeUser(Integer userId) {
        userStorage.removeUser(userId);
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

    //Вынесла бизнес логику методов дружбы из сервиса в Storage

    public void addToFriends(Integer userId, Integer friendId) {
        userStorage.getById(userId);
        userStorage.getById(friendId);
        if (userStorage.getById(userId).getFriends().contains(friendId)) {
            String message = "Пользователи уже дружат";
            log.error(message);
            throw new ValidationException(message);
        }
        userStorage.addToFriends(userId, friendId);
    }

    public void removeFromFriends(Integer userId, Integer friendId) {
        userStorage.getById(userId);
        userStorage.getById(friendId);
        userStorage.removeFromFriends(userId, friendId);
    }

    public List<User> findFriends(Integer id) {
        userStorage.getById(id);
        return userStorage.findFriends(id);
    }

    public List<User> findCommonFriends(Integer firstId, Integer secondId) {
        return userStorage.findCommonFriends(firstId, secondId);
    }
}
