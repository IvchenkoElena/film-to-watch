package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EventRowMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Event event = new Event();
        event.setEventId(resultSet.getInt("event_id"));
        event.setUserId(resultSet.getInt("user_id"));
        event.setTimestamp(resultSet.getLong("timestamp"));
        event.setEventType(EventType.valueOf(resultSet.getString("type")));
        event.setOperation(EventOperation.valueOf(resultSet.getString("operation")));
        event.setEntityId(resultSet.getInt("entity_id"));
        return event;
    }
}
