package org.sbtitcourses.mdwiki.repository;

import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для взаимодействия с сущностью {@link Page}.
 */
public interface PageRepository extends JpaRepository<Page, Integer> {

    /**
     * Поиск записи по названию.
     *
     * @param name имя записи.
     * @return найденую запись.
     */
    Optional<Page> findByName(String name);

    /**
     * Поиск публичных записей.
     *
     * @return список найденых записей.
     */
    List<Page> findBySharedTrue();

    /**
     * Поиск записи данного пространства.
     *
     * @param id    ID записи.
     * @param space пространство, в котором ищутся записи.
     * @return найденую запись.
     */
    Optional<Page> findByIdAndSpace(int id, Space space);

    /**
     * Поиск корневых записей данного пространства.
     *
     * @param space    пространство, в котором ищутся записи.
     * @param pageable объект, определяющий нужное колличество страниц.
     * @return список найденых записней.
     */
    List<Page> findBySpaceAndParentIsNullOrderById(Space space, Pageable pageable);

    /**
     * Поиск записи данного пространства по названию.
     *
     * @param space пространство, в котором ищутся записи.
     * @param name  имя записи.
     * @return найденую запись.
     */
    Optional<Page> findBySpaceAndName(Space space, String name);

    /**
     * Поиск записей данного пользователя по подстроке названия.
     *
     * @param owner подьзователь, у которго ищутся записи.
     * @param pageSearch подстрока имени записи.
     * @return Поиск записи данного пользователя по названию.
     */
    @Query("select page from Person p join Space s on p.id = s.owner.id join Page page on s.id = page.space.id where p = :owner and page.name like :pageSearch || '%' order by page.name")
    List<Page> findByOwnerAndNameStartingWith(Person owner, String pageSearch);
}