package org.sbtitcourses.mdwiki.repository;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.StoredFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для взаимодействия с сущностью StoredFile
 */
@Repository
public interface StoredFileRepository extends JpaRepository<StoredFile, Integer> {

    /**
     * Поиск по уникальному идентификатору файла
     * @param GUID уникальный идентификатор файла
     * @return найденый файл
     */
    Optional<StoredFile> findByGUID (String GUID);

    /**
     * Поиск по пользователю-владульцу файла
     * @param owner пользователь-владелец файла
     * @param pageable интерфейс для информации о пагинации
     * @return найденый файл
     */
    List<StoredFile> findByOwner (Person owner, Pageable pageable);
}
