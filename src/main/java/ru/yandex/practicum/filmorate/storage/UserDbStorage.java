package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

@Slf4j
@Repository("userDbStorage")
@Primary
public class UserDbStorage extends BaseRepository<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM USERS";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM USERS WHERE USER_ID = ?";
    private static final String UPDATE_QUERY = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ?" +
            "WHERE USER_ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO USERS(EMAIL, LOGIN, NAME, BIRTHDAY) VALUES(?, ?, ?, ?)";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO friendship(user_id, friend_id) VALUES(?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_FRIEND_QUERY = "SELECT u.* FROM users u JOIN friendship f ON f.friend_id = u.user_id WHERE f.user_id = ?";
    private static final String FIND_COMMON_FRIENDS_QUERY = "SELECT u.* FROM users u JOIN friendship f ON f.friend_id = u.user_id WHERE f.user_id = ? AND f.friend_id IN (SELECT friend_id FROM friendship WHERE user_id = ?)";


    // Инициализируем репозиторий
    @Autowired
    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User getById(int id) {
        return findOne(FIND_BY_ID_QUERY, id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c ID %d не найден", id)));
    }

    @Override
    public User update(@RequestBody User newUser) {
        update(
                UPDATE_QUERY,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday(),
                newUser.getId()
        );
        Set<Integer> friends = newUser.getFriends();
        if (friends != null) {
            for (int friendId : friends) {
                addToFriends(newUser.getId(), friendId);
            }
        }
        return newUser;
    }

    @Override
    public User save(User newUser) {
        Integer id = insert(
                INSERT_QUERY,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday()
        );
        newUser.setId(id);
        Set<Integer> friends = newUser.getFriends();
        if (friends != null) {
            for (int friendId : friends) {
                addToFriends(id, friendId);
            }
        }

        return newUser;
    }

    //Вынесла методы дружбы из сервиса в Storage

    @Override
    public void addToFriends(Integer userId, Integer friendId) {
        update(INSERT_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public void removeFromFriends(Integer userId, Integer friendId) {
        delete(DELETE_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public List<User> findFriends(Integer id) {
        return findMany(FIND_FRIEND_QUERY, id);
    }

    @Override
    public List<User> findCommonFriends(Integer firstId, Integer secondId) {
        return findMany(FIND_COMMON_FRIENDS_QUERY, firstId, secondId);
    }
}
