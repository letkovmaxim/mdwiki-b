package org.sbtitcourses.mdwiki.model;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;


/**
 * Сущность markdown-документа
 */
@Entity
@Table(name = "documents")
public class Document {

    /**
     * ID документа
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    /**
     * Текст markdown-документа
     */
    @Column(name = "text")
    private String text;

    /**
     * Запись, содержащая этот докумет
     */
    @OneToOne(fetch = EAGER)
    @JoinColumn(name = "page_id", referencedColumnName = "id", nullable = false)
    private Page page;

    public Document() {
    }

    public Document(Page page) {
        this.page = page;
    }

    public Document(String text) {
        this.text = text;
    }

    public Document(int id, Page page) {
        this.id = id;
        this.page = page;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
