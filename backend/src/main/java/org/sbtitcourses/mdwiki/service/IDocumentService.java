package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.util.ConvertedDocument;

/**
 * Интерфейс сервиса взаимодействия с сущностью {@link Document}.
 */
public interface IDocumentService {

    /**
     * Создать новой документ.
     *
     * @param document документ, который нужно сохранить.
     * @param pageId   ID страницы, в которой нужно создать документ.
     * @param spaceId  ID пространства, в котором нужно создать документ.
     * @return созданный документ.
     */
    Document create(Document document, int pageId, int spaceId);

    /**
     * Получить документ на странице.
     *
     * @param pageId  ID страницы, в которой нужно получить документ.
     * @param spaceId ID пространства, в котором нужно получить документ.
     * @return найденый документ.
     */
    Document get(int pageId, int spaceId);

    /**
     * Обновить документ на странице.
     *
     * @param pageId               ID страницы, в которой нужно обновить документ.
     * @param spaceId              ID пространства, в котором нужно обновить документ.
     * @param documentToUpdateWith информация о документе, которую нужно обновить.
     * @return обновленный документ.
     */
    Document update(int pageId, int spaceId, Document documentToUpdateWith);

    /**
     * Удалить документ на странице.
     *
     * @param pageId  ID страницы, в которой нужно удалить документ.
     * @param spaceId ID пространства, в котором нужно удалить документ.
     */
    void delete(int pageId, int spaceId);

    /**
     * Конвертировать докумет в PDF формат.
     *
     * @param spaceId  ID пространства.
     * @param pageId   ID страницы.
     * @param font     название шрифта.
     * @param fontSize размер шрифта в пикселях.
     * @return документа в виде ресурса.
     */
    ConvertedDocument convertToPdf(int spaceId, int pageId, String font, int fontSize, boolean tree);
}