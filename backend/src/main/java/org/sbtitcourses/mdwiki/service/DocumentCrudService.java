package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;

/**
 * Интерфейс сервиса CRUD операций над сущностью Document
 */
public interface DocumentCrudService {

    /**
     * Создание нового документа
     * @param documentToCreate документ, который нужно сохранить
     * @return созданный документ
     */
    Document create(Document documentToCreate);

    /**
     * Получение документа на странице
     * @param page страницы, на которой нужно получить документ
     * @return найденый документ
     */
    Document get(Page page);

    /**
     * Обновить документ на странице
     * @param page страница, на которой нужно обновить документ
     * @param documentToUpdateWith объект класса Document, значениями полей которого нужно обновить документ
     * @return обновленный документ
     */
    Document update(Page page, Document documentToUpdateWith);

    /**
     * Удаление документ на странице
     * @param page страница, на которой нужно удалить документ
     */
    void delete(Page page);
}
