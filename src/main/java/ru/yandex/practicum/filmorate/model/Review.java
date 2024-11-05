package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Review {
    private Integer reviewId;
    private String content;
    private Boolean isPositive;
    private Integer userId; // Пользователь
    private Integer filmId; // Фильм
    private Long useful; // Рейтинг полезности
}
