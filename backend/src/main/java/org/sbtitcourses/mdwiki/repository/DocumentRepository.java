package org.sbtitcourses.mdwiki.repository;

import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для взаимодействия с сущностью Document
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    /**
     * Поиск документа по странице
     * @param page страница, по которой нужно найти документ
     * @return найденую страницу
     */
    Optional<Document> findByPage(Page page);
}
