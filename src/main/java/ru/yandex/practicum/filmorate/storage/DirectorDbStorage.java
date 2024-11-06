package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Slf4j
@Repository
public class DirectorDbStorage extends BaseRepository<Director> implements DirectorStorage {
    private static final String FIND_ALL_DIRECTORS_QUERY = "SELECT * FROM directors";
    private static final String FIND_DIRECTOR_BY_ID = "SELECT * FROM directors WHERE director_id = ?";
    private static final String INSERT_DIRECTOR_QUERY = "INSERT INTO directors(director_name) VALUES (?)";
    private static final String UPDATE_DIRECTOR_QUERY = "UPDATE directors SET director_name = ?  WHERE director_id = ?";
    private static final String DELETE_DIRECTOR_QUERY =  "DELETE FROM directors WHERE director_id = ?";


    //Инициализируем репозиторий
    @Autowired
    public DirectorDbStorage(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
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
}
