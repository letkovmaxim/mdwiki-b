package org.sbtitcourses.mdwiki.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.GenerationType.IDENTITY;


/**
 * Сущность markdown-документа
 */
@Entity
@Table(name = "document")
public class Document {

    /**
     * ID документа
     */
    @Id
    @NotNull
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Текст markdown-документа
     */
    @Column(name = "document_text")
    private String text;

    /**
     * Запись, содержащая этот докумет
     */
    @OneToOne
    @JoinColumn(name = "page_id", referencedColumnName = "id")
    private Page page;

    public Document() {
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
