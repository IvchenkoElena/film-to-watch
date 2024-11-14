package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    public void setLikeOrDislike(Integer reviewId, Integer userId, Boolean isLike) {
        checkUserExistById(userId);
        reviewStorage.setLikeOrDislike(reviewId, userId, isLike);
    }

    public void deleteLikeOrDislike(Integer reviewId, Integer userId) {
        checkUserExistById(userId);
        reviewStorage.deleteLikeOrDislike(reviewId, userId);
    }

    void checkUserExistById(Integer userId) {
        userStorage.getById(userId);
    }

    void checkFilmExistById(Integer filmId) {
        filmStorage.getById(filmId);
    }

    void checkCorrectReviewParams(Review review) {
        Integer userId = review.getUserId();
        userStorage.getById(userId);

        Integer filmId = review.getFilmId();
        filmStorage.getById(filmId);
    }
}
