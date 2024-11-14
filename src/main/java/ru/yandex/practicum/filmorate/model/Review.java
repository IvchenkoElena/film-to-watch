package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Review {
    private Integer reviewId;
    @NotBlank
    private String content;
    @NotNull
    private Boolean isPositive;
    private Integer userId; // Пользователь
    private Integer filmId; // Фильм
    private Long useful = 0L; // Рейтинг полезности
}
