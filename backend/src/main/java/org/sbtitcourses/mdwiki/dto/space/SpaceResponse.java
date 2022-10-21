package org.sbtitcourses.mdwiki.dto.space;

import java.util.Date;

/**
 * DTO сущности Space для ответа
 */
public class SpaceResponse {

    /**
     * ID пространства
     */
    private int id;

    /**
     * Название пространства
     */
    private String name;

    /**
     * Точное время создания пространства
     */
    private Date createdAt;

    /**
     * Точное время обновления пространства
     */
    private Date updatedAt;

    /**
     * Статус публичности пространства
     */
    private Boolean isPublic;

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

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }
}
