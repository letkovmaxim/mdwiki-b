package org.sbtitcourses.mdwiki.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PageRequest {

    private String name;

    private Boolean isPublic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }
}
