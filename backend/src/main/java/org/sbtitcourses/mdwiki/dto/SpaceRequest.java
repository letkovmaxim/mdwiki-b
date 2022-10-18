package org.sbtitcourses.mdwiki.dto;

import org.sbtitcourses.mdwiki.model.Person;

import javax.validation.constraints.Size;

public class SpaceRequest {

    @Size(min = 1, max = 128, message = "Название не должен быть короче 4 и длинее 50 символов")
    private String name;
    private String isPublic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }
}
