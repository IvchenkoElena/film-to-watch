package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Slf4j
@Repository("reviewDbStorage")
public class ReviewDbStorage extends BaseRepository<Review> implements ReviewStorage {

    @Autowired
    public ReviewDbStorage(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Review addNew(Review newReview) {
        String query = "INSERT INTO reviews (content, is_positive, user_id, film_id, useful) VALUES (?, ?, ?, ?, ?)";
        Integer id = insert(
                query,
                newReview.getContent(),
                newReview.getIsPositive(),
                newReview.getUserId(),
                newReview.getFilmId(),
                newReview.getUseful()
        );
        newReview.setReviewId(id);
        return newReview;
    }

    @Override
    public Review update(Review modifiedReview) {
        String query = "UPDATE reviews SET content = ?, is_positive = ? WHERE review_id = ?";
        update(
                query,
                modifiedReview.getContent(),
                modifiedReview.getIsPositive(),
                modifiedReview.getReviewId()
        );
        //пришлось немного заморочиться, т.к. в базовом репозитории метод update() возвращает void
        return getById(modifiedReview.getReviewId());
    }

    @Override
    public void deleteById(Integer id) {
        String query = "DELETE FROM reviews WHERE review_id = ?";
        delete(query, id);
    }

    @Override
    public Review getById(Integer id) {
        String query = "SELECT * FROM reviews WHERE review_id = ?";
        return findOne(query, id).orElseThrow(() -> new NotFoundException(String.format("Отзыв c id %d не найден", id)));
    }

    @Override
    public List<Review> getByParams(Integer filmId, Integer count) {
        String query;
        if (filmId != null) {
            query = "SELECT * FROM reviews WHERE film_id = ? ORDER BY useful DESC LIMIT ?";
            return jdbc.query(query, ReviewRowMapper::makeReview, filmId, count);
        } else {
            query = "SELECT * FROM reviews ORDER BY useful DESC LIMIT ?";
            return jdbc.query(query, ReviewRowMapper::makeReview, count);
        }
    }

    @Override
    public void addLike(Integer reviewId, Integer userId) {
        String query = "UPDATE reviews SET useful = ? WHERE review_id = ?";
        Review modifiedReview = getById(reviewId);
        update(query, modifiedReview.getUseful() + 1, reviewId);
    }

    @Override
    public void addDislike(Integer reviewId, Integer userId) {
        String query = "UPDATE reviews SET useful = ? WHERE review_id = ?";
        Review modifiedReview = getById(reviewId);
        update(query, modifiedReview.getUseful() - 1, reviewId);
    }

    @Override
    public void deleteLike(Integer reviewId, Integer userId) {
        String query = "UPDATE reviews SET useful = ? WHERE review_id = ?";
        Review modifiedReview = getById(reviewId);
        update(query, modifiedReview.getUseful() - 1, reviewId);
    }

    @Override
    public void deleteDislike(Integer reviewId, Integer userId) {
        String query = "UPDATE reviews SET useful = ? WHERE review_id = ?";
        Review modifiedReview = getById(reviewId);
        update(query, modifiedReview.getUseful() + 1, reviewId);
    }
}
