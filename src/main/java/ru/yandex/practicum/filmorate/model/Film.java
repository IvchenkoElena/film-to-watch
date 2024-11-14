package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


@Data
public class Film {
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private Integer duration;
    private Set<Integer> likes = new HashSet<>();
    private Integer likesCount = 0;
    @NotNull
    private Mpa mpa;
    @NotNull
    private Set<Director> directors = new HashSet<>();
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();

    public void addGenre(Genre genre) {
        getGenres().add(genre);
    }

    public void addDirector(Director director) {
        getDirectors().add(director);
    }

    public void addLike(Integer userId) {
        getLikes().add(userId);
    }
}