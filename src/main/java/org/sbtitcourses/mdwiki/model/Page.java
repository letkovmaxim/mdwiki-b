package org.sbtitcourses.mdwiki.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * Сущность пользовательских записей.
 */
@Entity
@Table(name = "page")
public class Page {

    /**
     * ID записи
     */
    @Id
    @NotNull
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Название записи
     */
    @Column(name = "name")
    private String name;

    /**
     * Список подстраниц данной страницы
     */
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<Page> subpages;

    /**
     * Запись-родитель, которая содержит эту запись
     */
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Page parent;

    /**
     * Пространство, которое содержит эту запись
     */
    @ManyToOne
    @JoinColumn(name = "space_id", referencedColumnName = "id")
    private Space space;

    /**
     * Точное время создания записи
     */
    @Temporal(TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    /**
     * Точное время обновления записи
     */
    @Temporal(TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    /**
     * Статус публичности записи
     */
    @Column(name = "is_public")
    private Boolean isPublic;

    /**
     * Список документов, принадлжащих данной записи
     */
    @OneToOne(mappedBy = "page", fetch = FetchType.LAZY)
    private Document documents;

    public Page() {
    }

    public Page(String name, Boolean isPublic) {
        this.name = name;
        this.isPublic = isPublic;
    }

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

    public List<Page> getSubpages() {
        return subpages;
    }

    public void setSubpages(List<Page> subpages) {
        this.subpages = subpages;
    }

    public Page getParent() {
        return parent;
    }

    public void setParent(Page parent) {
        this.parent = parent;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
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
