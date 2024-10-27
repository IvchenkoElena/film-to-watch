package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Genre {
    private Integer id;
    @NotNull(message = "Название жанра не должно быть пустым")
    private String name;
}