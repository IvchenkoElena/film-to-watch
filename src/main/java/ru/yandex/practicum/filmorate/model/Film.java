package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;


/**
 * Film.
 */
@Data
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;


}
