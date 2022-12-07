package org.sbtitcourses.mdwiki.dto.person;

import javax.validation.constraints.NotEmpty;

/**
 * DTO с полем заметки сущности Person для запроса
 */
public class PersonNoteRequest {

    /**
     * Текст заметки markdown-документа
     */
    @NotEmpty(message = "Заметка не должна быть пустой")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
