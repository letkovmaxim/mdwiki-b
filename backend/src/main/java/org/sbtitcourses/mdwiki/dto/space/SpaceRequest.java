package org.sbtitcourses.mdwiki.dto.space;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * DTO сущности {@link org.sbtitcourses.mdwiki.model.Space} для запроса.
 */
public class SpaceRequest {

    /**
     * Название пространства.
     */
    @NotEmpty
    @Size(max = 128)
    private String name;

    /**
     * Статус публичности пространства.
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