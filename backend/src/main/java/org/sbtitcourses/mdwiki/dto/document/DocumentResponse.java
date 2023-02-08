package org.sbtitcourses.mdwiki.dto.document;

/**
 * DTO сущности {@link org.sbtitcourses.mdwiki.model.Document} для ответа.
 */
public class DocumentResponse {

    /**
     * Текст markdown-документа.
     */
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
