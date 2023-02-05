package org.sbtitcourses.mdwiki.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;
import static org.hibernate.annotations.CascadeType.ALL;

/**
 * Сущность, описывающая запись пользователя
 */
@Entity
@Table(name = "pages")
public class Page {

    /**
     * ID записи
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    /**
     * Название записи
     */
    @Column(name = "name", nullable = false)
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
    @JoinColumn(name = "space_id", referencedColumnName = "id", nullable = false)
    private Space space;

    /**
     * Точное время создания записи
     */
    @Temporal(TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    /**
     * Точное время обновления записи
     */
    @Temporal(TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    /**
     * Статус публичности записи
     */
    @Column(name = "shared", nullable = false)
    private boolean shared;

    /**
     * Список документов, принадлжащих данной записи
     */
    @OneToOne(mappedBy = "page", fetch = LAZY)
    @Cascade(ALL)
    private Document document;

    public Person getOwner() {
        return space.getOwner();
    }

    public Page() {
    }

    public Page(boolean shared) {
        this.shared = shared;
    }

    public Page(Space space) {
        this.space = space;
    }

    public Page(int id, Space space) {
        this.id = id;
        this.space = space;
    }

    public Page(String name, boolean shared) {
        this.name = name;
        this.shared = shared;
    }

    public Page(String name, Space space, boolean shared) {
        this.name = name;
        this.space = space;
        this.shared = shared;
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

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
