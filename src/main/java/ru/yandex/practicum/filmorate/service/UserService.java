package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final EventStorage eventStorage;
    private final FilmStorage filmStorage;
    private final GenreDbStorage genreStorage;
    private final DirectorStorage directorStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("eventDbStorage") EventStorage eventStorage,
                       @Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("genreDbStorage")GenreDbStorage genreStorage,
                       @Qualifier("directorDbStorage")DirectorStorage directorStorage) {
        this.userStorage = userStorage;
        this.eventStorage = eventStorage;
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.directorStorage = directorStorage;
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
        if (newUser.getName() == null || newUser.getName().isBlank()) {
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
        eventStorage.addEvent(new Event(userId, EventType.FRIEND, EventOperation.ADD, friendId));
    }

    public void removeFromFriends(Integer userId, Integer friendId) {
        userStorage.getById(userId);
        userStorage.getById(friendId);
        userStorage.removeFromFriends(userId, friendId);
        eventStorage.addEvent(new Event(userId, EventType.FRIEND, EventOperation.REMOVE, friendId));
    }

    public List<User> findFriends(Integer id) {
        userStorage.getById(id);
        return userStorage.findFriends(id);
    }

    public List<User> findCommonFriends(Integer firstId, Integer secondId) {
        return userStorage.findCommonFriends(firstId, secondId);
    }

    public List<Event> getEvents(int userId) {
        if (userStorage.getById(userId) == null) {
            String message = "Пользователь с ID " + userId + " не найден";
            throw new NotFoundException(message);
        }
        return eventStorage.getEvents(userId);
    }

    public List<Film> getRecommendedFilms(Integer userId) {
        userStorage.getById(userId);

        List<Film> recommendedFilms = filmStorage.getRecommendedFilms(userId);
        genreStorage.loadGenres(recommendedFilms);
        directorStorage.loadDirectors(recommendedFilms);

        return recommendedFilms;
    }
}
