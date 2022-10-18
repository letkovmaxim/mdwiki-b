package org.sbtitcourses.mdwiki.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.TemporalType.TIMESTAMP;
import static org.hibernate.annotations.CascadeType.ALL;

/**
 * Сущность пользовательских пространств.
 */
@Entity
@Table(name = "space")
public class Space {

    /**
     * ID пространства
     */
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "spaceSequence")
    @SequenceGenerator(name = "spaceSequence", initialValue = 1, allocationSize = 1, sequenceName = "space_sequence")
    @Column(name = "id", nullable = false)
    private int id;

    /**
     * Название пространства
     */
    @Column(name = "name")
    private String name;

    /**
     * Владелец данного пространства
     */
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Person owner;

    /**
     * Точное время создания пространства
     */
    @Temporal(TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    /**
     * Точное время обновления пространства
     */
    @Temporal(TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    /**
     * Статус публичности пространства
     */
    @Column(name = "is_public")
    private Boolean isPublic;

    /**
     * Список записей, принадлежащих данному пространству
     */
    @OneToMany(mappedBy = "space", fetch = LAZY)
    @Cascade(ALL)
    private List<Page> pages;

    public Space() {
    }

    public Space(int id) {
        this.id = id;
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

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
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

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
}
