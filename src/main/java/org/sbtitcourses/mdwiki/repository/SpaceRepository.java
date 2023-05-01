package org.sbtitcourses.mdwiki.repository;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для взаимодействия с сущностью {@link Space}.
 */
public interface SpaceRepository extends JpaRepository<Space, Integer> {

    /**
     * Поиск пространства по названию.
     *
     * @param name название пространства.
     * @return найденое пространтсво.
     */
    Optional<Space> findByName(String name);

    /**
     * Поиск публичных пространств.
     *
     * @param pageable объект, определяющий нужное колличество страниц.
     * @return список найденых пространтсв.
     */
    List<Space> findBySharedTrue(Pageable pageable);

    /**
     * Поиск пространств по владельцу.
     *
     * @param owner    владелец пространства.
     * @param pageable объект, определяющий нужное колличество страниц.
     * @return возвращает список найденых пространтсв.
     */
    List<Space> findByOwnerOrderById(Person owner, Pageable pageable);

    /**
     * Поиск пространства по владельцу и названию.
     *
     * @param owner владелец пространства.
     * @param name  название пространства.
     * @return возвращает найденое пространтсво.
     */
    Optional<Space> findByOwnerAndName(Person owner, String name);
}
