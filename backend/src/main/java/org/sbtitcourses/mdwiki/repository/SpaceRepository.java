package org.sbtitcourses.mdwiki.repository;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.springframework.data.domain.Pageable;
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
     * Поиск по id пространства
     * @param id id пространства
     * @return возвращает найденое пространтсво
     */
    Optional<Space> findById(int id);

    /**
     * Поиск публичных пространств
     * @return возвращает список найденых пространтсв
     */
    List<Space> findBySharedTrue();

    /**
     * Поиск публичных или пользовательских пространств
     * @return возвращает список найденых пространтсв
     */
    List<Space> findByOwnerOrSharedTrue(Person owner, Pageable pageable);

    /**
     * Поиск пространства и именя у авторизированного пользователя
     * @return возвращает найденое пространтсво
     */
    Optional<Space> findByOwnerAndName(Person owner, String name);
}
