package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {
    private Integer id;
    @NotBlank
    private String name;
    @NotNull
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private Integer duration;
    private Integer likesCount; //я должна прямо в базе данных теперь добавить такую колонку?
    //и в сервисе в методах добавления и снятия лайка делать изменения в БД?
    private final Set<Integer> likes = new HashSet<>(); //список лайков можно будет совсем удалить? он не проверяется в тестах?
    @NotNull
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();

    public void addGenre(Genre genre) {
        getGenres().add(genre);
    }
}
