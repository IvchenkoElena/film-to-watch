package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Event {
    private int id;
    private int userId;
    private long timestamp;
    private EventType eventType;
    private EventOperation operation;
    private int entityId;

    public Event() {
    }

    public Event(int userId, EventType eventType, EventOperation operation, int entityId) {
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
    }
}
