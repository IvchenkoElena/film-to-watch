package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public Review addNew(@RequestBody @Valid Review newReview) {
        log.info("Поступил запрос на добавление нового отзыва: {}", newReview);
        return reviewService.addNew(newReview);
    }

    @PutMapping
    public Review update(@RequestBody @Valid Review modifiedReview) {
        log.info("Поступил запрос на обновление отзыва: {}", modifiedReview.toString());
        return reviewService.update(modifiedReview);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Integer id) {
        log.info("Поступил запрос на удаление отзыва с id: {}", id);
        reviewService.deleteById(id);
    }

    @GetMapping("/{id}")
    public Review getById(@PathVariable Integer id) {
        log.info("Поступил запрос на получение отзыва с id: {}", id);
        return reviewService.getById(id);
    }

    @GetMapping
    public List<Review> getByParams(@RequestParam(required = false) Integer filmId,
                                    @RequestParam(defaultValue = "10") Integer count) {
        log.info("Поступил запрос на получение отзывов с параметрами filmId: {}, count: {}", filmId, count);
        return reviewService.getByParams(filmId, count);
    }

    @PutMapping("{reviewId}/like/{userId}")
    public void addLike(@PathVariable Integer reviewId, @PathVariable Integer userId) {
        log.info("Поступил запрос на добавление лайка отзыву с id: {} от пользователя с id: {}", reviewId, userId);
        Boolean isLike = true; //лайк
        reviewService.setLikeOrDislike(reviewId, userId, isLike);
    }

    @PutMapping("{reviewId}/dislike/{userId}")
    public void addDislike(@PathVariable Integer reviewId, @PathVariable Integer userId) {
        log.info("Поступил запрос на добавление дизлайка отзыву с id: {} от пользователя с id: {}", reviewId, userId);
        Boolean isLike = false; //дизлайк
        reviewService.setLikeOrDislike(reviewId, userId, isLike);
    }

    @DeleteMapping("{reviewId}/like/{userId}")
    public void deleteLike(@PathVariable Integer reviewId, @PathVariable Integer userId) {
        log.info("Поступил запрос на удаления лайка отзыву с id: {} от пользователя с id: {}", reviewId, userId);
        reviewService.deleteLikeOrDislike(reviewId, userId);
    }

    @DeleteMapping("{reviewId}/dislike/{userId}")
    public void deleteDislike(@PathVariable Integer reviewId, @PathVariable Integer userId) {
        log.info("Поступил запрос на удаление дизлайка отзыву с id: {} от пользователя с id: {}", reviewId, userId);
        reviewService.deleteLikeOrDislike(reviewId, userId);
    }
}
