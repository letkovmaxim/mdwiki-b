package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Space;

import java.util.List;

/**
 * Интерфейс сервиса CRUD операций над сущностью Space
 */
public interface SpaceCrudService {
    Space create(Space spaceToCreate);
    List<Space> getAll();
    Space get(int id);
    Space update(int id, Space spaceToUpdateWith);
    void delete(int id);
}
