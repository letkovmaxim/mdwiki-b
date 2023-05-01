package org.sbtitcourses.mdwiki.util;

import org.sbtitcourses.mdwiki.model.StoredFile;
import org.springframework.core.io.Resource;

/**
 * Вспомогательный класс для хранения файла в виде ресурса
 * и информации о нем в виде объекта класса {@link StoredFile}.
 */
public class LoadedFile {

    /**
     * Сущность записанных в системе файлов
     * для хранения информации о них в базе данных.
     */
    private final StoredFile storedFile;

    /**
     * Интерфейс, представляющий внешние ресурсы.
     */
    private final Resource resource;

    /**
     * Конструктор для создания объекта класса.
     *
     * @param storedFile сущность записанных в системе файлов
     *                   для хранения информации о них в базе данных.
     * @param resource   интерфейс, представляющий внешние ресурсы.
     */
    public LoadedFile(StoredFile storedFile, Resource resource) {
        this.storedFile = storedFile;
        this.resource = resource;
    }

    public StoredFile getStoredFile() {
        return storedFile;
    }

    public Resource getResource() {
        return resource;
    }
}