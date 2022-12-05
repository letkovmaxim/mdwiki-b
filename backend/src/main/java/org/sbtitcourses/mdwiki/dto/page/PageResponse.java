package org.sbtitcourses.mdwiki.dto.page;

import java.util.Date;
import java.util.List;

/**
 * DTO сущности Page для ответа
 */
public class PageResponse {

    /**
     * ID записи
     */
    private int id;

    /**
     * Название записи
     */
    private String name;

    /**
     * Список подстраниц данной страницы
     */
    private List<PageResponse> subpages;

    /**
     * Точное время создания записи
     */
    private Date createdAt;

    /**
     * Точное время обновления записи
     */
    private Date updatedAt;

    /**
     * Статус публичности записи
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

    public List<PageResponse> getSubpages() {
        return subpages;
    }

    public void setSubpages(List<PageResponse> subpages) {
        this.subpages = subpages;
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

    public boolean getShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }
}
