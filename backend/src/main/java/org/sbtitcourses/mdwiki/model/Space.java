package org.sbtitcourses.mdwiki.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;
import static org.hibernate.annotations.CascadeType.ALL;

/**
 * Сущность, описывающая пространство пользователя.
 */
@Entity
@Table(name = "spaces")
public class Space {

    /**
     * ID пространства.
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    /**
     * Название пространства.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Владелец данного пространства.
     */
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private Person owner;

    /**
     * Точное время создания пространства.
     */
    @Temporal(TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    /**
     * Точное время обновления пространства.
     */
    @Temporal(TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    /**
     * Статус публичности пространства
     */
    @Column(name = "shared", nullable = false)
    private boolean shared;

    /**
     * Список записей, принадлежащих данному пространству.
     */
    @OneToMany(mappedBy = "space", fetch = LAZY)
    @Cascade(ALL)
    private List<Page> pages;

    /**
     * Список хранимых файлов, связанных с данным пространством.
     **/
    @OneToMany(mappedBy = "space", fetch = LAZY)
    @Cascade(ALL)
    private List<StoredFile> storedFiles;

    public Space() {
    }

    public Space(String name, Person owner, boolean shared) {
        this.name = name;
        this.owner = owner;
        this.shared = shared;
    }

    public Space(int id) {
        this.id = id;
    }

    public Space(boolean shared) {
        this.shared = shared;
    }

    public Space(int id, boolean shared) {
        this.id = id;
        this.shared = shared;
    }

    public Space(String name, boolean shared) {
        this.name = name;
        this.shared = shared;
    }

    public Space(Person owner) {
        this.owner = owner;
    }

    public Space(int id, Person owner) {
        this.id = id;
        this.owner = owner;
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

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public List<StoredFile> getStoredFiles() {
        return storedFiles;
    }

    public void setStoredFiles(List<StoredFile> storedFiles) {
        this.storedFiles = storedFiles;
    }
}
