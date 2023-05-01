package org.sbtitcourses.mdwiki.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
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
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * Точное время обновления пространства.
     */
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

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

    public static SpaceBuilder builder() {
        return new SpaceBuilder();
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
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

    public static final class SpaceBuilder {
        private final Space space;

        private SpaceBuilder() {
            space = new Space();
        }

        public SpaceBuilder id(int id) {
            space.setId(id);
            return this;
        }

        public SpaceBuilder name(String name) {
            space.setName(name);
            return this;
        }

        public SpaceBuilder owner(Person owner) {
            space.setOwner(owner);
            return this;
        }

        public SpaceBuilder createdAt(Instant createdAt) {
            space.setCreatedAt(createdAt);
            return this;
        }

        public SpaceBuilder updatedAt(Instant updatedAt) {
            space.setUpdatedAt(updatedAt);
            return this;
        }

        public SpaceBuilder shared(boolean shared) {
            space.setShared(shared);
            return this;
        }

        public SpaceBuilder pages(List<Page> pages) {
            space.setPages(pages);
            return this;
        }

        public SpaceBuilder storedFiles(List<StoredFile> storedFiles) {
            space.setStoredFiles(storedFiles);
            return this;
        }

        public Space build() {
            return space;
        }
    }
}
