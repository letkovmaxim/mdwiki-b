package org.sbtitcourses.mdwiki.dto.person;

import javax.validation.constraints.NotEmpty;

/**
 * DTO с полем заметки сущности {@link org.sbtitcourses.mdwiki.model.Person} для запроса.
 */
public class PersonNoteRequest {

    /**
     * Текст заметки markdown-документа.
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
