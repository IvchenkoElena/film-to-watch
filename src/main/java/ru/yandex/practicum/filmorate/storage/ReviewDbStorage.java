package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Slf4j
@Repository("reviewDbStorage")
public class ReviewDbStorage extends BaseRepository<Review> implements ReviewStorage {

    @Autowired
    public ReviewDbStorage(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbcTemplate, RowMapper<Review> mapper) {
        super(jdbc, namedJdbcTemplate, mapper);
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
        //Пришлось немного заморочиться, т.к. в базовом репозитории метод update() возвращает void
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
        Boolean isLike = true;
        String query = "MERGE INTO REVIEW_LIKES_DISLIKES(review_id, user_id, is_like) KEY(REVIEW_ID,USER_ID) VALUES(?, ?, ?)";
        update(query, reviewId, userId, isLike);
        updateUsefulRateByReviewId(reviewId);
    }

    @Override
    public void addDislike(Integer reviewId, Integer userId) {
        Boolean isLike = false;
        String query = "MERGE INTO REVIEW_LIKES_DISLIKES(review_id, user_id, is_like) KEY(REVIEW_ID,USER_ID) VALUES(?, ?, ?)";
        update(query, reviewId, userId, isLike);
        updateUsefulRateByReviewId(reviewId);
    }

    @Override
    public void deleteLike(Integer reviewId, Integer userId) {
        String query = """
                UPDATE REVIEW_LIKES_DISLIKES
                SET IS_LIKE = NULL
                WHERE REVIEW_ID = ? AND USER_ID = ? AND IS_LIKE = TRUE
                """;
        update(query, reviewId, userId);
        updateUsefulRateByReviewId(reviewId);
    }

    @Override
    public void deleteDislike(Integer reviewId, Integer userId) {
        String query = """
                UPDATE REVIEW_LIKES_DISLIKES
                SET IS_LIKE = NULL
                WHERE REVIEW_ID = ? AND USER_ID = ? AND IS_LIKE = FALSE
                """;
        update(query, reviewId, userId);
        updateUsefulRateByReviewId(reviewId);
    }

    private Long updateUsefulRateByReviewId(Integer reviewId) {
        String query = """
                SELECT (SUM(is_like = TRUE) - SUM(is_like = FALSE))
                FROM REVIEW_LIKES_DISLIKES
                WHERE review_id = ?
                """;
        Long useful = jdbc.queryForObject(query, Long.class, reviewId);
        if (useful == null) {
            useful = 0L;
        }
        update("UPDATE reviews SET useful = ? WHERE review_id = ?", useful, reviewId);
        return useful;
    }
}
