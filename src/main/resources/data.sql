
SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE film_genre;

TRUNCATE TABLE friendship;

TRUNCATE TABLE likes;

TRUNCATE TABLE films RESTART IDENTITY;

TRUNCATE TABLE users RESTART IDENTITY;

TRUNCATE TABLE genres RESTART IDENTITY;

TRUNCATE TABLE rating RESTART IDENTITY;

TRUNCATE TABLE reviews RESTART IDENTITY;

TRUNCATE TABLE review_likes_dislikes;

SET REFERENTIAL_INTEGRITY TRUE;


merge into genres (genre_id, genre_name)
values (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');

merge into rating (rating_id, name, description)
values (1, 'G',      'У фильма нет возрастных ограничений'),                     -- 1
    (2, 'PG',    'Детям рекомендуется смотреть фильм с родителями'),              -- 2
    (3, 'PG-13', 'Детям до 13 лет просмотр не желателен'),                   -- 3
    (4, 'R',     'Лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),  -- 4
    (5, 'NC-17', 'Лицам до 18 лет просмотр запрещён');


INSERT INTO directors (director_name)
VALUES ('Стивен Спилберг'),
    ('Джеймс Кэмерон'),
    ('Тим Бёртон');

INSERT INTO FILMS (NAME , DESCRIPTION , RELEASE_DATE , DURATION , RATING_ID, LIKES_COUNT)
VALUES ('Тень', '30-ые годы XX века, город Нью-Йорк...', '1994-07-01', 108 , 3, 2),
    ('Звёздные войны: Эпизод 4 – Новая надежда', 'Татуин. Планета-пустыня. Уже постаревший рыцарь Джедай ...', '1997-05-25', 121 , 2, 3),
    ('Зеленая миля', 'Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора» ...', '1999-12-06', 189 , 4, 1),
    ('Гадкий я', 'Гадкий снаружи, но добрый внутри Грю намерен, тем не менее, ...', '2010-06-27', 95 , 2, 3);

INSERT INTO USERS (EMAIL , LOGIN , NAME , BIRTHDAY)
VALUES ('Capitan@yandex.ru', 'Capitan', 'Capitan', '2001-01-01'),   -- 1
    ('Jack@yandex.ru', 'Jack', 'Jack', '2002-02-02'),       -- 2
    ('Sparrow@yandex.ru', 'Sparrow', 'Sparrow', '2003-03-03');   -- 3

INSERT INTO LIKES (FILM_ID, USER_ID)
VALUES (1, 1), (1, 3),
    (2, 1), (2, 2), (2, 3),
    (3, 2),
    (4, 1), (4, 2), (4, 3);

INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID)
VALUES (1, 2), (1, 3),
    (2, 1),
    (3, 1), (3, 2);

INSERT INTO FILM_GENRE (FILM_ID , GENRE_ID)
VALUES (1, 2), (1, 4), (1, 6),
    (2, 2), (2, 4), (2, 6),
    (3, 1), (3, 2), (3, 4),
    (4, 1), (4, 3), (4, 4), (4, 6);