package org.sbtitcourses.mdwiki.repository;

import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Space;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для взаимодействия с сущностью Page
 */
@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {

    /**
     * Поиск записи по названию
     * @param name имя записи
     * @return возвращает найденую запись
     */
    Optional<Page> findByName(String name);

    /**
     * Поиск публичных записей
     * @return возвращает список найденых записнй
     */
    List<Page> findByIsPublicTrue();

    /**
     * Поиск записи данного пространства
     * @param id ID записи
     * @param space пространство, в котором ищутся записи
     * @return возвращает найденую запись
     */
    Optional<Page> findByIdAndSpace(int id, Space space);

    /**
     * Поиск корневых записей данного пространства
     * @param space пространство, в котором ищутся записи
     * @param pageable объект, определяющий нужное колличество страниц
     * @return возвращает список найденых записней
     */
    List<Page> findBySpaceAndParentIsNull(Space space, Pageable pageable);
}