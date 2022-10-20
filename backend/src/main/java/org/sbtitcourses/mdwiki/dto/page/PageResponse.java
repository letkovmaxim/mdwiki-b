package org.sbtitcourses.mdwiki.dto.page;

import java.util.Date;
import java.util.List;

public class PageResponse {

    private int id;

    private String name;

    private List<PageResponse> subpages;

    private Date createdAt;

    private Date updatedAt;

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

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }
}
