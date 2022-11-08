package org.sbtitcourses.mdwiki.dto.space;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * DTO сущности Space для запроса
 */
public class SpaceRequest {

    /**
     * Название пространства
     */
    @NotEmpty(message = "Название не должно быть пустым")
    @Size(max = 128, message = "Название не должно быть длиннее 128 символов")
    private String name;

    /**
     * Статус публичности пространства
     */
    private boolean shared;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }
}