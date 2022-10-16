package org.sbtitcourses.mdwiki.service;

import java.util.List;

public interface CrudService<T> {
    T create(T entity);
    List<T> getAll();
    T get(int id);
    T update(int id, T entity);
    void delete(int id);
}
