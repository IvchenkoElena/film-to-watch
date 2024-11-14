package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    Review addNew(@RequestBody Review newReview);

    Review update(@RequestBody Review modifiedReview);

    void deleteById(Integer id);

    Review getById(Integer id);

    List<Review> getByParams(Integer filmId, Integer count);

    void setLikeOrDislike(Integer reviewId, Integer userId, Boolean isLike); //Пользователь ставит лайк отзыву.

    void deleteLikeOrDislike(Integer reviewId, Integer userId); //Пользователь удаляет лайк отзыву.
}
