package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Person;

import java.util.List;

/**
 * Интерфейс сервиса CRUD операций над сущностью Person
 */
public interface PersonCrudService {

    /**
     * Получение всех пользователей
     * @return список всех пользователей
     */
    List<Person> getAll();

    /**
     * Получение пользователя по его ID
     * @param id ID пользователя
     * @return найденного пользователя
     */
    Person get(int id);

    /**
     * Обновление пользователя по его ID
     * @param id ID пользователя
     * @param personToUpdateWith объект класса Person, значениями полей которого нужно обновить пользователя
     * @return обновленного пользователя
     */
    Person update(int id, Person personToUpdateWith);

    /**
     * Обновление заметки пользователя по его ID
     * @param id ID пользователя
     * @param note обновленная заметка
     * @return обновленного пользователя
     */
    Person noteUpdate(int id, String note);

    /**
     * Удаление пользователя по его ID
     * @param id ID пользователя
     */
    void delete(int id);
}
