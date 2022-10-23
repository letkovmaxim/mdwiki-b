package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Person;

import java.util.List;

/**
 * Интерфейс сервиса CRUD операций над сущностью Person
 */
public interface PersonCrudService {
    Person create(Person personToCreate);
    List<Person> getAll();
    Person get(int id);
    Person update(int id, Person personToUpdateWith);
    void delete(int id);
}
