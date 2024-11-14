package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository("directorDbStorage")
public class DirectorDbStorage extends BaseRepository<Director> implements DirectorStorage {
    private static final String FIND_ALL_DIRECTORS_QUERY = "SELECT * FROM directors";
    private static final String FIND_DIRECTOR_BY_ID = "SELECT * FROM directors WHERE director_id = ?";
    private static final String INSERT_DIRECTOR_QUERY = "INSERT INTO directors(director_name) VALUES (?)";
    private static final String UPDATE_DIRECTOR_QUERY = "UPDATE directors SET director_name = ?  WHERE director_id = ?";
    private static final String DELETE_DIRECTOR_QUERY = "DELETE FROM directors WHERE director_id = ?";
    private static final String FIND_FILM_DIRECTORS_QUERY = "SELECT d.*, fd.FILM_ID from directors d, film_director fd where d.director_id = fd.director_id AND fd.FILM_ID in ";

    //Инициализируем репозиторий
    @Autowired
    public DirectorDbStorage(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbcTemplate, RowMapper<Director> mapper) {
        super(jdbc, namedJdbcTemplate, mapper);
    }

    @Override
    public List<Director> findAllDirectors() {
        return findMany(FIND_ALL_DIRECTORS_QUERY);
    }

    @Override
    public Director findDirectorById(Integer directorId) {
        return findOne(FIND_DIRECTOR_BY_ID, directorId)
                .orElseThrow(() -> new NotFoundException(String.format("Режиссер с ID %d не найден", directorId)));
    }

    @Override
    public Director createDirector(Director newDirector) {
        Integer id = insert(
                INSERT_DIRECTOR_QUERY,
                newDirector.getName()
        );
        newDirector.setId(id);
        return newDirector;
    }

    @Override
    public Director updateDirector(@RequestBody Director newDirector) {
        update(
                UPDATE_DIRECTOR_QUERY,
                newDirector.getName(),
                newDirector.getId()
        );
        return newDirector;
    }

    @Override
    public void removeDirector(Integer directorId) {
        boolean queryResult = delete(DELETE_DIRECTOR_QUERY, directorId);
        if (!queryResult) {
            throw new NotFoundException(String.format("Режиссер с ID %d не найден", directorId));
        }
    }

    public void loadDirectors(List<Film> films) {
        final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, film -> film));
        List<Integer> filmIds = films.stream().map(Film::getId).toList();
        SqlParameterSource parameters = new MapSqlParameterSource("filmIds", filmIds);

        namedJdbcTemplate.query(FIND_FILM_DIRECTORS_QUERY + "(:filmIds)", parameters, (rs) -> {
            final Film film = filmById.get(rs.getInt("FILM_ID"));
            film.addDirector(mapper.mapRow(rs, 0));
        });
    }
}
