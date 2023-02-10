package org.sbtitcourses.mdwiki.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.annotations.CascadeType.ALL;

/**
 * Сущность, описывающая запись пользователя.
 */
@Entity
@Table(name = "pages")
public class Page {

    /**
     * ID записи.
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    /**
     * Название записи.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Список подстраниц данной страницы.
     */
    @OneToMany(mappedBy = "parent", fetch = LAZY)
    @Cascade(ALL)
    private List<Page> subpages;

    /**
     * Запись-родитель, которая содержит эту запись.
     */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Page parent;

    /**
     * Пространство, которое содержит эту запись.
     */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "space_id", referencedColumnName = "id", nullable = false)
    private Space space;

    /**
     * Точное время создания записи.
     */
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * Точное время обновления записи.
     */
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Статус публичности записи.
     */
    @Column(name = "shared", nullable = false)
    private boolean shared;

    /**
     * Список документов, принадлжащих данной записи.
     */
    @OneToOne(mappedBy = "page", fetch = LAZY)
    @Cascade(ALL)
    private Document document;

    public Page() {
    }

    public static PageBuilder builder() {
        return new PageBuilder();
    }

    public Person getOwner() {
        return space.getOwner();
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

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public static final class PageBuilder {
        private final Page page;

        private PageBuilder() {
            page = new Page();
        }

        public PageBuilder id(int id) {
            page.setId(id);
            return this;
        }

        public PageBuilder name(String name) {
            page.setName(name);
            return this;
        }

        public PageBuilder subpages(List<Page> subpages) {
            page.setSubpages(subpages);
            return this;
        }

        public PageBuilder parent(Page parent) {
            page.setParent(parent);
            return this;
        }

        public PageBuilder space(Space space) {
            page.setSpace(space);
            return this;
        }

        public PageBuilder createdAt(Instant createdAt) {
            page.setCreatedAt(createdAt);
            return this;
        }

        public PageBuilder updatedAt(Instant updatedAt) {
            page.setUpdatedAt(updatedAt);
            return this;
        }

        public PageBuilder shared(boolean shared) {
            page.setShared(shared);
            return this;
        }

        public PageBuilder document(Document document) {
            page.setDocument(document);
            return this;
        }

        public Page build() {
            return page;
        }
    }
}
