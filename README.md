# [![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&weight=600&size=40&pause=10000&color=36A2F7&width=435&height=56&lines=FILMORATE)](https://git.io/typing-svg)
Template repository for Filmorate project.

## Схема базы данных 
![Схема базы данных](https://github.com/IvchenkoElena/java-filmorate/blob/main/src/main/resources/Filmorate%20(2).png)

## Описание
User - информация о пользователях

Friendship - информация о дружбе между пользователями и ее статусе (подтвержденная или не подтвержденная)

Likes - информация об оценках фильмов пользователями

Film - информация о фильмах

Rating - описание возрастных рейтингов

Film_genre - информация о жанрах фильмов

Genre - названия жанров

## Примеры SQL запросов


#### Найти пользователя по идентификатору: findUser(Long userId)
```sql
SELECT *
FROM User
WHERE user_id = {userId};
```
#### Добавить нового пользователя: create(User user)
```sql
INSERT INTO User (email, login, name, birthday)
VALUE ({user.getEmail()}, {user.getLogin()}, {user.getName()}, {user.getBirthday()});
```

#### Обновить данные пользователя: update(User user)
```sql
UPDATE User 
SET email = {user.getEmail()}, 
    login = {user.getLogin()}, 
    name = {user.getName()}, 
    birthday = {user.getBirthday()}
WHERE user_id = {user.getId()};
```

#### Удалить пользователя по идентификатору: deleteUser (Long userId)
```sql
DELETE FROM User
WHERE user_id = {userId};
```
