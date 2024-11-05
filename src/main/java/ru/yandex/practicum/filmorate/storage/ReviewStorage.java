package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    Review addNew(@RequestBody Review newReview);

    Review update(@RequestBody Review modifiedReview);

    void deleteById(Long id);

    Review getById(Long id);

    //Получение всех отзывов по идентификатору фильма, если фильм не указан то все. Если кол-во не указано, то 10.
    List<Review> getByParams(Long filmId, Long count);

    void addLike (Long reviewId, Long userId); //Пользователь ставит лайк отзыву.

    void addDislike (Long reviewId, Long userId); //Пользователь ставит дизлайк отзыву.

    void deleteLike (Long reviewId, Long userId); //Пользователь удаляет лайк отзыву.

    void deleteDislike (Long reviewId, Long userId); //Пользователь удаляет дизлайк отзыву.
}
