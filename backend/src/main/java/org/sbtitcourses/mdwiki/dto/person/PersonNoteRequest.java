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
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
