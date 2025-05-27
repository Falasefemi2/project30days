package com.femiproject.tasktracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Task {

    private String id;
    private String description;
    private StatusEnum status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public Task(String id, String description, StatusEnum status) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("id cannot be null or empty");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("description cannot be null or empty");
        }
        if (status == null) {
            throw new IllegalArgumentException("status cannot be null");
        }

        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Task(String description, StatusEnum status) {
        this(UUID.randomUUID().toString(), description, status);
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createdAtStr = createdAt.format(formatter);
        String updatedAtStr = updatedAt.format(formatter);

        return "Task [id=" + id + ", description=" + description + ", status=" + status + ", createdAt=" + createdAtStr
                + ", updatedAt=" + updatedAtStr + "]";
    }
}
