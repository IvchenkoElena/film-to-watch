package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

@Repository("eventDbStorage")
public class EventDbStorage extends BaseRepository<Event> implements EventStorage {
    public EventDbStorage(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbcTemplate, RowMapper<Event> mapper) {
        super(jdbc, namedJdbcTemplate, mapper);
    }

    @Override
    public void addEvent(Event event) {
        String query = "INSERT INTO events(user_id, timestamp, type, operation, entity_id) VALUES(?, ?, ?, ?, ?)";
        insert(query, event.getUserId(), event.getTimestamp(), event.getEventType().toString(),
                event.getOperation().toString(), event.getEntityId());
    }

    @Override
    public List<Event> getEvents(int userId) {
        String query = "SELECT e.* FROM EVENTS e WHERE e.user_id = ? ORDER BY e.timestamp";
        return findMany(query, userId);
    }
}
