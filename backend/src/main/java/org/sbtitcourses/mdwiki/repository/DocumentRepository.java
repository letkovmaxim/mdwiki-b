package org.sbtitcourses.mdwiki.repository;

import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для взаимодействия с сущностью {@link Document}.
 */
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    /**
     * Поиск документа по странице.
     *
     * @param page страница, по которой нужно найти документ.
     * @return найденую страницу.
     */
    Optional<Document> findByPage(Page page);
}
