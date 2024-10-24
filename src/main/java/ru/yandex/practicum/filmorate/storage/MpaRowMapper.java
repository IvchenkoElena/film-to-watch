package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRowMapper implements RowMapper<Mpa> {


    @Override
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
         Mpa mpa = new Mpa();
         mpa.setId(rs.getInt("rating_id"));
         mpa.setName(rs.getString("name"));
         mpa.setDescription(rs.getString("description"));
         return mpa;
    }
}