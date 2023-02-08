package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.StoredFile;
import org.sbtitcourses.mdwiki.util.LoadedFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Интерфейс сервиса с логикой записи и получения файлов.
 */
public interface IImageStorageService {

    /**
     * Метод, отвечающий за создание пользовательских директорий в файловой системе для хранения изображений,
     * запись изображения в соответствующию директорию, создание превью для этого изображения,
     * сохранение информации об изображении в базу данных.
     *
     * @param file            файл изображения.
     * @param spaceId         ID пространства, с которым связано изображение.
     * @param thumbnailHeight высота превью изображения.
     * @param thumbnailWidth  ширина превью изображения.
     * @return объек с информацией о записанном изображении.
     */
    StoredFile storeImage(MultipartFile file, int spaceId, int thumbnailHeight, int thumbnailWidth);

    /**
     * Метод, отвечающий за получение изображения в виде ресурса и информацию о нем.
     *
     * @param GUID уникальный идентификатор изображения.
     * @return объект с изображением в виде ресурса и информацию о нем.
     */
    LoadedFile loadImage(String GUID);

    /**
     * Метод, отвечающий за получение изображения в виде ресурса и информацию о нем.
     *
     * @param GUID уникальный идентификатор изображения.
     * @return объект с превью изображения в виде ресурса и информацию о нем.
     */
    LoadedFile loadThumbnail(String GUID);

    /**
     * Метод, отвечающий за удаление изображения и его превтю из файловой системы
     * и информации о нем из базы данных.
     *
     * @param GUID уникальный идентификатор изображения.
     */
    void deleteImage(String GUID);

    /**
     * Метод, отвечающий за получение информации обо всех загруженных пользователем файлов.
     *
     * @param bunch номер страницы при пагинации.
     * @param size  количество элементов в странице при пагинации.
     */
    List<StoredFile> getUserStoredFiles(int bunch, int size);
}