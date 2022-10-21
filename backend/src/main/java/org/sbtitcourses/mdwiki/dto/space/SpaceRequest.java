package org.sbtitcourses.mdwiki.dto.space;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "Значение public должно быть true или false")
    private Boolean isPublic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }
}