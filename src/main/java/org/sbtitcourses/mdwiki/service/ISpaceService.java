package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Space;

import java.util.List;

/**
 * Интерфейс сервиса взаимодействия с сущностью {@link Space}.
 */
public interface ISpaceService {

    /**
     * Создать новое пространство.
     *
     * @param space пространство, которое нужно сохранить.
     * @return созданное пространство.
     */
    Space create(Space space);

    /**
     * Получить все пространства пользователя.
     *
     * @param bunch номер страницы при пагинации.
     * @param size  количество элементов в странице при пагинации.
     * @return список всех пространств пользователя.
     */
    List<Space> get(int bunch, int size);

    /**
     * Получить все публичные пространства
     *
     * @param bunch номер страницы при пагинации.
     * @param size  количество элементов в странице при пагинации.
     * @return список всех публичных пространств.
     */
    List<Space> getShared(int bunch, int size);

    /**
     * Получить пространство по его ID.
     *
     * @param id ID пространства.
     * @return найденное пространство.
     */
    Space get(int id);

    /**
     * Обновить пространство по его ID.
     *
     * @param id                ID пространства.
     * @param spaceToUpdateWith информация о пространстве, которую нужно обновить.
     * @return обновленное пространство.
     */
    Space update(int id, Space spaceToUpdateWith);

    /**
     * Удалить пространство по его ID.
     *
     * @param id ID пространства.
     */
    void delete(int id);
}