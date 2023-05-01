package org.sbtitcourses.mdwiki.repository;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.StoredFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для взаимодействия с сущностью {@link StoredFile}.
 */
public interface StoredFileRepository extends JpaRepository<StoredFile, Integer> {

    /**
     * Поиск файла по уникальному идентификатору.
     *
     * @param GUID уникальный идентификатор файла.
     * @return найденый файл.
     */
    Optional<StoredFile> findByGUID(String GUID);

    /**
     * Поиск файла по владульцу.
     *
     * @param owner    владелец файла.
     * @param pageable объект, определяющий нужное колличество страниц.
     * @return найденый файл.
     */
    List<StoredFile> findByOwner(Person owner, Pageable pageable);
}
