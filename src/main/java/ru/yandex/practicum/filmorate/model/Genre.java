package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Genre {
    private Integer id;
    @NotBlank(message = "Название жанра не должно быть пустым")
    private String name;
}