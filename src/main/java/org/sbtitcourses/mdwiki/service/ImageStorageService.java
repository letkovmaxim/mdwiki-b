package org.sbtitcourses.mdwiki.service;

import net.coobird.thumbnailator.Thumbnails;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.model.StoredFile;
import org.sbtitcourses.mdwiki.repository.StoredFileRepository;
import org.sbtitcourses.mdwiki.util.EntityFetcher;
import org.sbtitcourses.mdwiki.util.LoadedFile;
import org.sbtitcourses.mdwiki.util.ResourceAccessHelper;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.sbtitcourses.mdwiki.util.exception.FileStorageException;
import org.sbtitcourses.mdwiki.util.exception.UnsupportedTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * Сервис с логикой записи и получения файлов.
 */
@Service
@Transactional(readOnly = true)
public class ImageStorageService implements IImageStorageService {

    /**
     * Репозиторий для взаимодействия с сущностью {@link StoredFile}.
     */
    private final StoredFileRepository storedFileRepository;

    /**
     * Компонент для получения сущностей.
     */
    private final EntityFetcher entityFetcher;

    /**
     * Директория для хранения загруженных изображений.
     */
    private final String uploadsDirectory;

    /**
     * Директория для хранения превью загруженных изображений.
     */
    private final String thumbnailsDirectory;

    /**
     * Конструктор для автоматического внедрения зависимостей.
     *
     * @param storedFileRepository репозиторий для взаимодействия с сущностью {@link StoredFile}.
     * @param entityFetcher        компонент для получения ресурсов.
     * @param uploadsDirectory     директория для хранения загруженных изображений.
     * @param thumbnailsDirectory  директория для хранения превью загруженных изображений.
     */
    @Autowired
    public ImageStorageService(StoredFileRepository storedFileRepository,
                               EntityFetcher entityFetcher,
                               @Value("${file.uploads-directory}") String uploadsDirectory,
                               @Value("${file.thumbnails-directory}") String thumbnailsDirectory) {
        this.storedFileRepository = storedFileRepository;
        this.entityFetcher = entityFetcher;
        this.uploadsDirectory = uploadsDirectory;
        this.thumbnailsDirectory = thumbnailsDirectory;
    }

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
     * @throws AccessDeniedException если не удалось определить пользователя.
     * @throws FileStorageException  если произошла ошибка записи файла.
     */
    @Override
    @Transactional
    public StoredFile storeImage(MultipartFile file, int spaceId,
                                 int thumbnailHeight, int thumbnailWidth) {
        if (isFileNotAnImage(file)) {
            throw new UnsupportedTypeException("Недопустимый тип файла");
        }

        if (file.getOriginalFilename() == null) {
            throw new FileStorageException("Недопустимое имя файла");
        }

        Space space = entityFetcher.fetchSpace(spaceId);
        Person user = entityFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToUpdateSpaceDenied(space, user)) {
            throw new AccessDeniedException("Доступ запрещен");
        }

        String userDirectory = getUserDirectory(user);
        Path imageStorageLocation = Path.of(uploadsDirectory).resolve(userDirectory);
        Path thumbnailStorageLocation = imageStorageLocation.resolve(thumbnailsDirectory);

        try {
            Files.createDirectories(imageStorageLocation);
            Files.createDirectories(thumbnailStorageLocation);
        } catch (IOException e) {
            throw new FileStorageException("Ошибка создания директорий для хранения файлов");
        }

        try {
            String GUID = UUID.randomUUID().toString();
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileMimeType = file.getContentType();

            Path imageLocation = renameAndResolveLocation(GUID, originalFileName, imageStorageLocation);
            Files.copy(file.getInputStream(), imageLocation);

            StoredFile storedFile = new StoredFile(GUID, originalFileName,
                    fileMimeType, file.getSize(), user, space);

            storedFileRepository.save(storedFile);

            Path thumbnailLocation = renameAndResolveLocation(GUID, originalFileName, thumbnailStorageLocation);
            Thumbnails.of(file.getInputStream())
                    .size(thumbnailHeight, thumbnailWidth)
                    .toFile(thumbnailLocation.toFile());

            return storedFile;
        } catch (IOException e) {
            throw new FileStorageException("Ошибка записи файла");
        }
    }

    /**
     * Метод, отвечающий за получение изображения в виде ресурса и информацию о нем.
     *
     * @param GUID уникальный идентификатор изображения.
     * @return объект с изображением в виде ресурса и информацию о нем.
     * @throws ElementNotFoundException если не удалось найти изображение.
     * @throws AccessDeniedException    если не удалось определить пользователя.
     */
    @Override
    public LoadedFile loadImage(String GUID) {
        Person user = entityFetcher.getLoggedInUser();
        StoredFile storedFile = storedFileRepository.findByGUID(GUID)
                .orElseThrow(() -> new ElementNotFoundException("Файл не найден"));

        if (ResourceAccessHelper.isAccessToReadSpaceDenied(storedFile.getSpace(), user)) {
            throw new AccessDeniedException("Доступ запрещен");
        }

        String userDirectory = getUserDirectory(user);
        Path imageStorageLocation = Paths.get(uploadsDirectory).resolve(userDirectory);
        Path imageLocation = renameAndResolveLocation(GUID, storedFile.getOriginalName(), imageStorageLocation);

        Resource resource = getResource(imageLocation);
        return new LoadedFile(storedFile, resource);
    }

    /**
     * Метод, отвечающий за получение изображения в виде ресурса и информацию о нем.
     *
     * @param GUID уникальный идентификатор изображения.
     * @return объект с превью изображения в виде ресурса и информацию о нем.
     * @throws ElementNotFoundException если не удалось найти изображение.
     * @throws AccessDeniedException    если не удалось определить пользователя.
     */
    @Override
    public LoadedFile loadThumbnail(String GUID) {
        Person user = entityFetcher.getLoggedInUser();
        StoredFile storedFile = storedFileRepository.findByGUID(GUID)
                .orElseThrow(() -> new ElementNotFoundException("Файл не найден"));

        if (ResourceAccessHelper.isAccessToReadSpaceDenied(storedFile.getSpace(), user)) {
            throw new AccessDeniedException("Доступ запрещен");
        }

        String userDirectory = getUserDirectory(user);
        Path thumbnailStorageLocation = Paths.get(uploadsDirectory).resolve(userDirectory).resolve(thumbnailsDirectory);
        Path thumbnailLocation = renameAndResolveLocation(GUID, storedFile.getOriginalName(), thumbnailStorageLocation);

        Resource resource = getResource(thumbnailLocation);
        return new LoadedFile(storedFile, resource);
    }

    /**
     * Метод, отвечающий за удаление изображения и его превью
     * из файловой системы и информации о нем из базы данных.
     *
     * @param GUID уникальный идентификатор изображения.
     * @throws ElementNotFoundException если не удалось найти изображение.
     * @throws AccessDeniedException    если не удалось определить пользователя.
     */
    @Override
    @Transactional
    public void deleteImage(String GUID) {
        Person user = entityFetcher.getLoggedInUser();
        StoredFile storedFile = storedFileRepository.findByGUID(GUID)
                .orElseThrow(() -> new ElementNotFoundException("Файл не найден"));

        if (ResourceAccessHelper.isAccessToUpdateSpaceDenied(storedFile.getSpace(), user)) {
            throw new AccessDeniedException("Доступ запрещен");
        }

        String userDirectory = getUserDirectory(user);
        Path imageStorageLocation = Paths.get(uploadsDirectory).resolve(userDirectory);
        Path imageLocation = renameAndResolveLocation(GUID, storedFile.getOriginalName(), imageStorageLocation);

        Path thumbnailStorageLocation = imageStorageLocation.resolve(thumbnailsDirectory);
        Path thumbnailLocation = renameAndResolveLocation(GUID, storedFile.getOriginalName(), thumbnailStorageLocation);

        try {
            Files.delete(imageLocation);
            Files.delete(thumbnailLocation);
        } catch (IOException e) {
            throw new ElementNotFoundException("Ошибка удаления файла");
        }

        storedFileRepository.delete(storedFile);
    }

    /**
     * Метод, отвечающий за получение информации обо всех загруженных пользователем файлов.
     *
     * @param bunch номер страницы при пагинации.
     * @param size  количество элементов в странице при пагинации.
     * @return список объектов с информацией о файлах.
     */
    @Override
    public List<StoredFile> getUserStoredFiles(int bunch, int size) {
        Person user = entityFetcher.getLoggedInUser();
        Pageable pageable = PageRequest.of(bunch, size);

        return storedFileRepository.findByOwner(user, pageable);
    }

    private boolean isFileNotAnImage(MultipartFile file) {
        return file.getContentType() == null || !file.getContentType().startsWith("image/");
    }

    private Path renameAndResolveLocation(String newFileName, String originalFileName, Path fileStorageLocation) {
        String targetExtension = StringUtils.getFilenameExtension(originalFileName);
        String targetName = newFileName;

        if (targetExtension != null) {
            targetName = targetName.concat(".").concat(targetExtension);
        }

        return fileStorageLocation.resolve(targetName);
    }

    private Resource getResource(Path targetLocation) {
        try {
            Resource resource = new UrlResource(targetLocation.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new ElementNotFoundException("Файл не найден");
            }
        } catch (MalformedURLException e) {
            throw new ElementNotFoundException("Файл не найден");
        }
    }

    private String getUserDirectory(Person user) {
        return user.getUsername();
    }
}