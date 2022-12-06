package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Space;

import java.util.List;

/**
 * Интерфейс сервиса CRUD операций над сущностью Space
 */
public interface SpaceCrudService {

    /**
     * Создание нового пространства
     * @param space пространство, которое нужно сохранить
     * @return созданное пространство
     */
    Space create(Space space);

    /**
     * Получение всех пространств пользователя
     * @param bunch номер страницы при пагинации
     * @param size количество элементов в странице при пагинации
     * @return список всех пространств пользователя
     */
    List<Space> get(int bunch, int size);

    /**
     * Получение всех публичных пространств
     * @param bunch номер страницы при пагинации
     * @param size количество элементов в странице при пагинации
     * @return список всех публичных пространств
     */
    List<Space> getShared(int bunch, int size);

    /**
     * Получение пространства по его ID
     * @param id ID пространства
     * @return найденное пространство
     */
    Space get(int id);

    /**
     * Обновление пространства по его ID
     * @param id ID пространства
     * @param spaceToUpdateWith объект класса Space, значениями полей которого нужно обновить пространство
     * @return обновленное пространство
     */
    Space update(int id, Space spaceToUpdateWith);

    /**
     * Удаление пространства по его ID
     * @param id ID пространства
     */
    void delete(int id);
}
