package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.StoredFile;
import org.sbtitcourses.mdwiki.property.FileStorageProperties;
import org.sbtitcourses.mdwiki.repository.StoredFileRepository;
import org.sbtitcourses.mdwiki.util.ResourceAccessHelper;
import org.sbtitcourses.mdwiki.util.ResourceFetcher;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.sbtitcourses.mdwiki.util.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final StoredFileRepository storedFileRepository;
    private final ResourceFetcher resourceFetcher;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties, StoredFileRepository storedFileRepository, ResourceFetcher resourceFetcher) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.storedFileRepository = storedFileRepository;
        this.resourceFetcher = resourceFetcher;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new FileStorageException("Ошибка создания директории для хранения файлов");
        }
    }

    public StoredFile storeFileOnPage(MultipartFile file, int pageId, int spaceId) {
        Page page = resourceFetcher.fetchPage(pageId, spaceId);
        Person user = resourceFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToUpdatePageDenied(page, user)) {
            throw new AccessDeniedException("Доступ запрещен");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Название файла содержит недопустимую последовательнось символов");
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String GUID = UUID.randomUUID().toString();
            StoredFile storedFile = new StoredFile(fileName, GUID, user, Collections.singletonList(page));

            storedFileRepository.save(storedFile);

            return storedFile;
        } catch (IOException e) {
            throw new FileStorageException("Ошибка записи файла");
        }
    }

    public Resource loadFileAsResourceFromPage(String GUID, int pageId, int spaceId) {
        Page page = resourceFetcher.fetchPage(pageId, spaceId);
        Person user = resourceFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToReadPageDenied(page, user)) {
            throw new AccessDeniedException("Доступ запрещен");
        }

        StoredFile storedFile = storedFileRepository.findByGUID(GUID)
                .orElseThrow(() -> new ElementNotFoundException("Файл не найден"));

        try {
            Path filePath = this.fileStorageLocation.resolve(storedFile.getName()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new ElementNotFoundException("Файл не найден");
            }
        } catch (MalformedURLException e) {
            throw new ElementNotFoundException("Файл не найден");
        }
    }
}
