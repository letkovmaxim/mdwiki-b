package org.sbtitcourses.mdwiki.dto.document;

import javax.validation.constraints.NotEmpty;

/**
 * DTO сущности {@link org.sbtitcourses.mdwiki.model.Document} для запроса.
 */
public class DocumentRequest {

    /**
     * Текст markdown-документа.
     */
    @NotEmpty
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
