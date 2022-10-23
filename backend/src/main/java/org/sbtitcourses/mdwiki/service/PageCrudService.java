package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Space;

import java.util.List;

/**
 * Интерфейс сервиса CRUD операций над сущностью Page
 */
public interface PageCrudService {
    Page create(Page pageToCreate);
    List<Page> getAll(Space space, int bunch, int size);
    Page get(int id, Space space);
    Page update(int id, Space space, Page pageToUpdateWith);
    void delete(int id);
}
