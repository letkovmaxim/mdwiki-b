package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Person;

import java.util.List;

/**
 * Интерфейс сервиса взаимодействия с сущностью {@link Person}.
 */
public interface IPersonService {

    /**
     * Получить всех пользователей.
     *
     * @return список всех пользователей.
     */
    List<Person> get(int bunch, int size);

    /**
     * Получить пользователя по его ID.
     *
     * @param id ID пользователя.
     * @return найденного пользователя.
     */
    Person get(int id);

    /**
     * Обновить пользователя по его ID.
     *
     * @param id                 ID пользователя.
     * @param personToUpdateWith информация о пользователе, которую нужно обновить.
     * @return обновленного пользователя.
     */
    Person update(int id, Person personToUpdateWith);

    /**
     * Обновить заметку пользователя по его ID.
     *
     * @param id   ID пользователя.
     * @param note обновленная заметка.
     * @return обновленного пользователя.
     */
    Person noteUpdate(int id, String note);

    /**
     * Удалить пользователя по его ID.
     *
     * @param id ID пользователя.
     */
    void delete(int id);
}