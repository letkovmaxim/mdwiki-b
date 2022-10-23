package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Space;

import java.util.List;

/**
 * Интерфейс сервиса CRUD операций над сущностью Page
 */
public interface PageCrudService {

    /**
     * Создание новой страницы
     * @param pageToCreate страница, которую нужно сохранить
     * @return созданную страницу
     */
    Page create(Page pageToCreate);

    /**
     * Получение всех страниц в пространстве
     * @param space пространство, в котором нужно получить страницы
     * @param bunch номер группы, на которые разбиваются страницы
     * @param size количество страниц в группе
     * @return список найденых страниц
     */
    List<Page> getAll(Space space, int bunch, int size);

    /**
     * Получение страницы в пространстве по её ID
     * @param id ID страницы
     * @param space пространство, в котором нужно найти страницу
     * @return найденую страницу
     */
    Page get(int id, Space space);

    /**
     * Обновление странцы в пространстве по её ID
     * @param id ID страницы
     * @param space пространство, в котором нужно обновить страницу
     * @param pageToUpdateWith объект Page, значениями полей которого нужно обновить страницу
     * @return обновленную страницу
     */
    Page update(int id, Space space, Page pageToUpdateWith);

    /**
     * Удаление страницы по её ID
     * @param id ID страницы
     */
    void delete(int id);
}
