package org.sbtitcourses.mdwiki.dto.page;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * DTO сущности {@link org.sbtitcourses.mdwiki.model.Page} для запроса.
 */
public class PageRequest {

    /**
     * Название записи.
     */
    @NotEmpty
    @Size(max = 128)
    private String name;

    /**
     * Статус публичности записи.
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
