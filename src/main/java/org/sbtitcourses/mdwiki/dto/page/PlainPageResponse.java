package org.sbtitcourses.mdwiki.dto.page;

import java.time.Instant;

/**
 * DTO сущности {@link org.sbtitcourses.mdwiki.model.Page} для ответа без списка подстраниц.
 */
public class PlainPageResponse {

    /**
     * ID записи.
     */
    private int id;

    /**
     * Название записи.
     */
    private String name;


    /**
     * Точное время создания записи.
     */
    private Instant createdAt;

    /**
     * Точное время обновления записи.
     */
    private Instant updatedAt;

    /**
     * Статус публичности записи.
     */
    private boolean shared;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }
}
