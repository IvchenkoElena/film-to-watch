package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DirectorRowMapper implements RowMapper<Director> {
    @Override
    public Director mapRow(ResultSet rs, int rowNum) throws SQLException {
        Director director = new Director();
        director.setId(rs.getInt("director_id"));
        director.setName(rs.getString("director_name"));
        return director;
    }
}
