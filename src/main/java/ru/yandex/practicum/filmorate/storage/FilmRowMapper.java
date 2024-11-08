package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;


@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("rating_id"));
        mpa.setName(rs.getString("RATING_NAME"));
        mpa.setDescription(rs.getString("RATING_DESCRIPTION"));
        film.setMpa(mpa);
        film.setLikesCount(rs.getInt("likes_count"));
        film.setDirectors(new HashSet<>());
        film.setGenres(new LinkedHashSet<>());
        film.setLikes(new HashSet<>());
        return film;
    }
}