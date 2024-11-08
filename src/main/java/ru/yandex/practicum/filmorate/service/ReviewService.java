package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final EventStorage eventStorage;

    public Review addNew(Review newReview) {
        checkCorrectReviewParams(newReview);
        Review review = reviewStorage.addNew(newReview);
        eventStorage.addEvent(new Event(review.getUserId(), EventType.REVIEW, EventOperation.ADD, review.getReviewId()));
        return review;
    }

    public Review update(Review modifiedReview) {
        Review review = reviewStorage.update(modifiedReview);
        eventStorage.addEvent(new Event(review.getUserId(), EventType.REVIEW, EventOperation.UPDATE, review.getReviewId()));
        return review;
    }

    public void deleteById(Integer id) {
        Review review = reviewStorage.getById(id);
        reviewStorage.deleteById(id);
        eventStorage.addEvent(new Event(review.getUserId(), EventType.REVIEW, EventOperation.REMOVE, review.getReviewId()));
    }

    public Review getById(Integer id) {
        return reviewStorage.getById(id);
    }

    public List<Review> getByParams(Integer filmId, Integer count) {
        return reviewStorage.getByParams(filmId, count);
    }

    public void addLike(Integer reviewId, Integer userId) {
        checkUserExistById(userId);
        reviewStorage.addLike(reviewId, userId);
    }

    public void addDislike(Integer reviewId, Integer userId) {
        checkUserExistById(userId);
        reviewStorage.addDislike(reviewId, userId);
    }

    public void deleteLike(Integer reviewId, Integer userId) {
        checkUserExistById(userId);
        reviewStorage.deleteLike(reviewId, userId);
    }

    public void deleteDislike(Integer reviewId, Integer userId) {
        checkUserExistById(userId);
        reviewStorage.deleteDislike(reviewId, userId);
    }

    void checkUserExistById(Integer userId) {
        if (userId <= 0) {
            throw new NotFoundException(String.format("Пользователь c ID %d не найден", userId));
        }
        userStorage.getById(userId);
    }

    void checkFilmExistById(Integer filmId) {
        if (filmId <= 0) {
            throw new NotFoundException(String.format("Фильм c ID %d не найден", filmId));
        }
        filmStorage.getById(filmId);
    }

    void checkCorrectReviewParams(Review review) {
        Integer userId = review.getUserId();
        if (userId <= 0) {
            throw new NotFoundException(String.format("Пользователь c ID %d не найден", userId));
        }
        userStorage.getById(userId);

        Integer filmId = review.getFilmId();
        if (filmId <= 0) {
            throw new NotFoundException(String.format("Фильм c ID %d не найден", filmId));
        }
        filmStorage.getById(filmId);

        String content = review.getContent();
        if (content == null || content.isBlank()) {
            throw new ValidationException("Невозможно добавить пустой отзыв");
        }

        Boolean isPositive = review.getIsPositive();
        if (isPositive == null) {
            throw new ValidationException("Невозможно добавить отзыв без указания типа отзыва");
        }
    }
}
