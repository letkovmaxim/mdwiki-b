package org.sbtitcourses.mdwiki.dto.document;

import javax.validation.constraints.NotEmpty;

/**
 * DTO сущности Document для запроса
 */
public class DocumentRequest {

    /**
     * Текст markdown-документа
     */
    @NotEmpty(message = "Текст документа не должен быть пустым")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
