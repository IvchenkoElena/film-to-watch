package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("userInMemoryStorage")
@RequiredArgsConstructor
public class UserInMemoryStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User save(User newUser) {
        // формируем дополнительные данные
        newUser.setId(getNextId());
        // сохраняем нового пользователя в памяти приложения
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User getById(int id) {
        User user = users.get(id);
        if (user == null) {
            String message = "Пользователь с id = " + id + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        return user;
    }

    public User update(User newUser) {
        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            String message = "Пользователь с id = " + newUser.getId() + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        // если публикация найдена и все условия соблюдены, обновляем её содержимое
        oldUser.setName(newUser.getName());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setBirthday(newUser.getBirthday());
        return oldUser;
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

    //Вынесла методы дружбы из сервиса в Storage

    public void addToFriends(Integer userId, Integer friendId) {
        getById(userId).getFriends().add(friendId);
    }

    public void removeFromFriends(Integer userId, Integer friendId) {
        getById(userId).getFriends().remove(friendId);
    }

    public List<User> findFriends(Integer id) {
        return getById(id).getFriends().stream()
                .map(this::getById)
                .toList();
    }

    public List<User> findCommonFriends(Integer firstId, Integer secondId) {
        return getById(firstId).getFriends().stream()
                .filter(id -> getById(secondId).getFriends().contains(id))
                .map(this::getById)
                .toList();
    }
}
