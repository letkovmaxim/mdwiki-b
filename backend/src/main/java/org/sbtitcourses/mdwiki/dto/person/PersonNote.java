package org.sbtitcourses.mdwiki.dto.person;

/**
 * DTO сущности Person для заметки пользователя
 */

public class PersonNote {

    /**
     * Текст заметки markdown-документа
     */
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
