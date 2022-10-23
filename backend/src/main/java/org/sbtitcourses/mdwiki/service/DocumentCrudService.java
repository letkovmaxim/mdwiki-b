package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;

/**
 * Интерфейс сервиса CRUD операций над сущностью Document
 */
public interface DocumentCrudService {
    Document create(Document documentToCreate);
    Document get(Page page);
    Document update(Page page, Document documentToUpdateWith);
    void delete(Page page);
}
