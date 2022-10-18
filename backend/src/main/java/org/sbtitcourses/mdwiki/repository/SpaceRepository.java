package org.sbtitcourses.mdwiki.repository;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для взаимодействия с сущностью Space
 */
@Repository
public interface SpaceRepository extends JpaRepository<Space, Integer> {

    /**
     * Поиск по названию пространства
     * @param name имя пространства
     * @return возвращает найденое пространтсво
     */
    Optional<Space> findByName(String name);

    /**
     * Поиск публичных пространств
     * @return возвращает список найденых пространтсв
     */
    List<Space> findByIsPublicTrue();

    /**
     * Поиск пространств, принадлежащих пользователю
     * @return возвращает список найденых пространтсв
     */
    List<Space> findByOwner(Person owner);
}
