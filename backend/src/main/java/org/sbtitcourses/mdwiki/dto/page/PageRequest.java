package org.sbtitcourses.mdwiki.dto.page;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * DTO сущности Page для запроса
 */
public class PageRequest {

    /**
     * Название записи
     */
    @NotEmpty(message = "Название не должно быть пустым")
    @Size(max = 128, message = "Название не должно быть длиннее 128 символов")
    private String name;

    /**
     * Статус публичности записи
     */
    private boolean shared;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }
}
