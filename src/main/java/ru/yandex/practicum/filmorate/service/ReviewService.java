package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;

    public Review addNew(Review newReview) {
        return reviewStorage.addNew(newReview);
    }

    public Review update(Review modifiedReview) {
        return reviewStorage.update(modifiedReview);
    }

    public void deleteById(Integer id) {
        reviewStorage.deleteById(id);
    }

    public Review getById(Integer id) {
        return reviewStorage.getById(id);
    }

    public List<Review> getByParams(Integer filmId, Integer count) {
        return reviewStorage.getByParams(filmId, count);
    }

    public void addLike(Integer reviewId, Integer userId) {
        reviewStorage.addLike(reviewId, userId);
    }

    public void addDislike(Integer reviewId, Integer userId) {
        reviewStorage.addDislike(reviewId, userId);
    }

    public void deleteLike(Integer reviewId, Integer userId) {
        reviewStorage.deleteLike(reviewId, userId);
    }

    public void deleteDislike(Integer reviewId, Integer userId) {
        reviewStorage.deleteDislike(reviewId, userId);
    }
}
