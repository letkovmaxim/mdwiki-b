package org.sbtitcourses.mdwiki.model;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * Сущность, описывающая markdown-документ пользователя.
 */
@Entity
@Table(name = "documents")
public class Document {

    /**
     * ID документа.
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    /**
     * Текст markdown-документа.
     */
    @Column(name = "text")
    private String text;

    /**
     * Запись, содержащая этот докумет.
     */
    @OneToOne(fetch = EAGER)
    @JoinColumn(name = "page_id", referencedColumnName = "id", nullable = false)
    private Page page;

    public Document() {
    }

    public static DocumentBuilder builder() {
        return new DocumentBuilder();
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

    public static final class DocumentBuilder {
        private final Document document;

        private DocumentBuilder() {
            document = new Document();
        }

        public DocumentBuilder id(int id) {
            document.setId(id);
            return this;
        }

        public DocumentBuilder text(String text) {
            document.setText(text);
            return this;
        }

        public DocumentBuilder page(Page page) {
            document.setPage(page);
            return this;
        }

        public Document build() {
            return document;
        }
    }
}
