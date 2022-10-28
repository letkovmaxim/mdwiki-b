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
    private boolean isPublic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}
