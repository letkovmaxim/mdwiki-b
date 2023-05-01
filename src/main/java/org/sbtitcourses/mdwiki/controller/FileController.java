package org.sbtitcourses.mdwiki.controller;

import org.sbtitcourses.mdwiki.dto.file.FileUploadResponse;
import org.sbtitcourses.mdwiki.model.StoredFile;
import org.sbtitcourses.mdwiki.service.ImageStorageService;
import org.sbtitcourses.mdwiki.util.LoadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * REST контроллер, обрабатывающий запросы на загрузку и скачивание файлов.
 */
@RestController
@Validated
public class FileController {

    /**
     * Сервис с логикой записи и получения файлов.
     */
    private final ImageStorageService imageStorageService;

    /**
     * Конструктор для автоматического внедрения зависимостей.
     *
     * @param imageStorageService сервис с логикой записи и получения файлов.
     */
    @Autowired
    public FileController(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    /**
     * Метод, обрабатывающий запрос на загрузку изображения.
     *
     * @param spaceId         ID пространства, с которым связано изображение.
     * @param file            файл изображения.
     * @param thumbnailHeight высота превью изображения.
     * @param thumbnailWidth  ширина превью изображения.
     * @return HTTP ответ с информацией о загруженном изображении и статусом 200.
     */
    @PostMapping("/spaces/{spaceId}/upload/image")
    public ResponseEntity<FileUploadResponse> uploadImage(@PathVariable("spaceId") int spaceId,
                                                          @RequestParam("file") MultipartFile file,
                                                          @RequestParam("thumbnailHeight") int thumbnailHeight,
                                                          @RequestParam("thumbnailWidth") int thumbnailWidth) {
        StoredFile storedFile = imageStorageService.storeImage(file, spaceId, thumbnailHeight, thumbnailWidth);

        FileUploadResponse response = new FileUploadResponse(storedFile.getOriginalName(), storedFile.getGUID(),
                storedFile.getMimeType(), file.getSize());

        return ResponseEntity.ok().body(response);
    }

    /**
     * Метод, обрабатывающий запрос на скачку изображения.
     *
     * @param GUID уникальный идентификатор изображения.
     * @return HTTP ответ с изображением и статусом 200.
     */
    @GetMapping("/download/image/{GUID}")
    public ResponseEntity<Resource> downloadImage(@PathVariable("GUID") String GUID) {
        LoadedFile loadedFile = imageStorageService.loadImage(GUID);

        String fileName = loadedFile.getStoredFile().getOriginalName();
        String contentType = loadedFile.getStoredFile().getMimeType();
        Resource resource = loadedFile.getResource();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"".concat(fileName).concat("\""))
                .body(resource);
    }

    /**
     * Метод, обрабатывающий запрос на скачку превью изображения.
     *
     * @param GUID уникальный идентификатор изображения.
     * @return HTTP ответ с превью изображения и статусом 200.
     */
    @GetMapping("/download/thumbnail/{GUID}")
    public ResponseEntity<Resource> downloadThumbnail(@PathVariable("GUID") String GUID) {
        LoadedFile loadedFile = imageStorageService.loadThumbnail(GUID);

        String fileName = loadedFile.getStoredFile().getOriginalName();
        String contentType = loadedFile.getStoredFile().getMimeType();
        Resource resource = loadedFile.getResource();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"".concat(fileName).concat("\""))
                .body(resource);
    }

    /**
     * Метод, обрабатывающий запрос на получение информации обо всех загруженных пользователем файлов.
     *
     * @param bunch номер страницы при пагинации.
     * @param size  количество элементов в странице при пагинации.
     * @return HTTP ответ со списком файлов и статусом 200.
     */
    @GetMapping("/user/uploads")
    public ResponseEntity<List<FileUploadResponse>>
    getUserStoredFiles(@RequestParam("bunch") @Min(0) int bunch,
                       @RequestParam("size") @Min(1) int size) {
        List<StoredFile> storedFiles = imageStorageService.getUserStoredFiles(bunch, size);
        List<FileUploadResponse> response = new ArrayList<>();

        for (StoredFile storedFile : storedFiles) {
            response.add(new FileUploadResponse(storedFile.getOriginalName(), storedFile.getGUID(),
                    storedFile.getMimeType(), storedFile.getSize()));
        }

        return ResponseEntity.ok().body(response);
    }

    /**
     * Метод, обрабатывающий запрос на удаления изображения.
     *
     * @param GUID уникальный идентификатор изображения.
     * @return HTTP ответ со статусом 200.
     */
    @DeleteMapping("/delete/image/{GUID}")
    public ResponseEntity<HttpStatus> deleteFile(@PathVariable("GUID") String GUID) {
        imageStorageService.deleteImage(GUID);

        return ResponseEntity.ok().build();
    }
}
