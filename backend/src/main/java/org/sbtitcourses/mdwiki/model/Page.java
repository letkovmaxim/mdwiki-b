package org.sbtitcourses.mdwiki.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.TemporalType.TIMESTAMP;
import static org.hibernate.annotations.CascadeType.ALL;

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
    @GeneratedValue(strategy = SEQUENCE, generator = "pageSequence")
    @SequenceGenerator(name = "pageSequence", initialValue = 1, allocationSize = 1, sequenceName = "page_sequence")
    @Column(name = "id", nullable = false)
    private int id;

    /**
     * Название записи
     */
    @Column(name = "name")
    private String name;

    /**
     * Список подстраниц данной страницы
     */
    @OneToMany(mappedBy = "parent", fetch = LAZY)
    @Cascade(ALL)
    private List<Page> subpages;

    /**
     * Запись-родитель, которая содержит эту запись
     */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Page parent;

    /**
     * Пространство, которое содержит эту запись
     */
    @ManyToOne(fetch = EAGER)
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
    @OneToOne(mappedBy = "page", fetch = LAZY)
    @Cascade(ALL)
    private Document document;

    public Page() {
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

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
