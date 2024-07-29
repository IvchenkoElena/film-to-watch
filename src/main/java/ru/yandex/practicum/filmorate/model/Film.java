package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private final Set<Integer> likes = new HashSet<>(); // по совету одногруппников добавила тут final, чтобы сразу создавался пустой список.
    // Но не до конца понимаю, почему именно так работает, а пока не было final, мог возвращаться null
}
