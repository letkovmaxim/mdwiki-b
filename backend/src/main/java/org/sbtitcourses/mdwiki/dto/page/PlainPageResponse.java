package org.sbtitcourses.mdwiki.dto.page;

import java.util.Date;

/**
 * DTO сущности Page для ответа без списка подстраниц.
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
    private Date createdAt;

    /**
     * Точное время обновления записи.
     */
    private Date updatedAt;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }
}
