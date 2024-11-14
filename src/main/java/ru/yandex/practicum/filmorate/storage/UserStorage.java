package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> findAll();

    User save(@RequestBody User newUser);

    User getById(int id);

    User update(@RequestBody User newUser);

    void removeUser(Integer userId);

    //Вынесла методы дружбы из сервиса в Storage

    void addToFriends(Integer userId, Integer friendId);

    void removeFromFriends(Integer userId, Integer friendId);

    List<User> findFriends(Integer id);

    List<User> findCommonFriends(Integer firstId, Integer secondId);
}
