package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    Review addNew(@RequestBody Review newReview);

    Review update(@RequestBody Review modifiedReview);

    void deleteById(Integer id);

    Review getById(Integer id);

    //Получение всех отзывов по идентификатору фильма, если фильм не указан то все. Если кол-во не указано, то 10.
    List<Review> getByParams(Integer filmId, Integer count);

    void addLike (Integer reviewId, Integer userId); //Пользователь ставит лайк отзыву.

    void addDislike (Integer reviewId, Integer userId); //Пользователь ставит дизлайк отзыву.

    void deleteLike (Integer reviewId, Integer userId); //Пользователь удаляет лайк отзыву.

    void deleteDislike (Integer reviewId, Integer userId); //Пользователь удаляет дизлайк отзыву.
}
