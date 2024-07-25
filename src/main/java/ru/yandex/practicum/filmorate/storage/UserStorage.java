package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public List<User> findAll();
    public User save(@RequestBody User newUser);
    public User getById (long id);
    public User update(@RequestBody User newUser);




}
