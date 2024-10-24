package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public List<Mpa> findAll() {
        log.info("Получение списка рейтингов");
        return mpaService.findAllMpa();
    }

    @GetMapping("/{mpaId}")
    public Mpa findById(@PathVariable Integer mpaId) {
        log.info("Получение рейтинга с ID {}", mpaId);
        return mpaService.findById(mpaId);
    }
}
