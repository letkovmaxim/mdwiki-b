package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Page;

import java.util.List;

/**
 * Интерфейс сервиса взаимодействия с сущностью {@link Page}.
 */
public interface IPageService {

    /**
     * Создать новую страницу.
     *
     * @param page    страница, которую нужно сохранить.
     * @param spaceId ID пространства, в котором нужно создать страницу.
     * @return созданную страницу.
     */
    Page create(Page page, int spaceId);

    /**
     * Создать новою подстраницу.
     *
     * @param subpage  подстраница, которую нужно сохранить.
     * @param parentId ID страницы-родителя, для которого нужно создать подстраницу.
     * @param spaceId  ID пространства, в котором нужно создать подстраницу.
     * @return созданую подстраницу.
     */
    Page createSubpage(Page subpage, int parentId, int spaceId);

    /**
     * Получить все страницы в пространстве.
     *
     * @param spaceId ID пространства, в котором нужно получить страницы.
     * @param bunch   номер группы, на которые разбиваются страницы.
     * @param size    количество страниц в группе.
     * @return список найденых страниц.
     */
    List<Page> get(int spaceId, int bunch, int size);

    /**
     * Получить все найденные страницы пользователя.
     *
     * @param pageSearch подстрока, для поиска страницы.
     * @return список найденых страниц.
     */
    List<Page> get(String pageSearch);

    /**
     * Получить страницу-родителя.
     *
     * @param id      ID страница, родителя которой нужно получить.
     * @param spaceId ID пространтсва, в котором нужно получить страницу.
     * @return найденую страницу.
     */
    Page getParent(int id, int spaceId);

    /**
     * Получить страницу в пространстве по её ID.
     *
     * @param id      ID страницы.
     * @param spaceId ID пространства, в котором нужно найти страницу.
     * @return найденую страницу.
     */
    Page get(int id, int spaceId);

    /**
     * Обновить странцу в пространстве по её ID.
     *
     * @param id               ID страницы.
     * @param spaceId          ID пространства, в котором нужно обновить страницу.
     * @param pageToUpdateWith информация о странице, которую нужно обновить.
     * @return обновленную страницу.
     */
    Page update(int id, int spaceId, Page pageToUpdateWith);

    /**
     * Удалить страницу по её ID.
     *
     * @param id      ID страницы.
     * @param spaceId ID пространства, в котором нужно удалить страницу.
     */
    void delete(int id, int spaceId);
}