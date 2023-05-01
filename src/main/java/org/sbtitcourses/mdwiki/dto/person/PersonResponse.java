package org.sbtitcourses.mdwiki.dto.person;

import java.time.Instant;

/**
 * DTO сущности {@link org.sbtitcourses.mdwiki.model.Person} для ответа.
 */
public class PersonResponse {

    /**
     * ID пользователя в базе данных.
     */
    private int id;

    /**
     * Логин пользователя.
     */
    private String username;

    /**
     * Имя пользователя.
     */
    private String name;

    /**
     * Эл. почта пользователя.
     */
    private String email;

    /**
     * Точное время создания пользователя.
     */
    private Instant createdAt;

    /**
     * Точное время обновления пользователя.
     */
    private Instant updatedAt;

    /**
     * Статус активности аккаунта.
     */
    private boolean enabled;

    /**
     * Текст заметки markdown-документа.
     */
    private String note;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
