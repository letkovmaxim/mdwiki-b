package org.sbtitcourses.mdwiki.controller;

import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.document.DocumentRequest;
import org.sbtitcourses.mdwiki.dto.document.DocumentResponse;
import org.sbtitcourses.mdwiki.model.ConvertedDocument;
import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * REST контроллер для CRUD операций над сущностью Document
 */
@RestController
@RequestMapping("/spaces/{spaceId}/pages/{pageId}/document")
@Validated
public class DocumentController {

    /**
     * Сервис с логикой CRUD операций над сущностью Document
     */
    private final DocumentService documentService;
    /**
     * Маппер для конвертации сущностей
     */
    private final ModelMapper modelMapper;

    /**
     * Конструктор для автоматичекого внедрения зависимостей
     * @param documentService сервис с логикой CRUD операций над сущностью Document
     * @param modelMapper маппер для конвертации сущностей
     */
    @Autowired
    public DocumentController(DocumentService documentService, ModelMapper modelMapper) {
        this.documentService = documentService;
        this.modelMapper = modelMapper;
    }

    /**
     * Метод, отвечающий за создание нового документа
     * @param spaceId ID пространства, в котором нужно создать документ
     * @param pageId ID страницы, в которой нужно создать дорумент
     * @param documentRequest DTO сущности Document для запроса
     * @return DTO сущности Document для ответа с кодом 201
     */
    @PostMapping
    public ResponseEntity<DocumentResponse> create(@PathVariable(name = "spaceId") int spaceId,
                                                   @PathVariable(name = "pageId") int pageId,
                                                   @RequestBody @Valid DocumentRequest documentRequest) {
        Document document = modelMapper.map(documentRequest, Document.class);

        Document createdDocument = documentService.create(document, pageId, spaceId);

        DocumentResponse response = modelMapper.map(createdDocument, DocumentResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Метод, отвечающий за получение страницы по его ID для данной страницы и пространства
     * @param spaceId ID пространства
     * @param pageId ID страницы
     * @return DTO сущности Page для ответа с кодом 200
     */
    @GetMapping
    public ResponseEntity<DocumentResponse> get(@PathVariable(name = "spaceId") int spaceId,
                                                @PathVariable(name = "pageId") int pageId) {
        Document document = documentService.get(pageId, spaceId);

        DocumentResponse response = modelMapper.map(document, DocumentResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *  Метод, отвечающий за обновление документа с заданым ID для данной страницы и пространства
     * @param spaceId ID пространства
     * @param pageId ID страницы
     * @param documentRequest DTO сущности Document для запроса
     * @return DTO сущности Document для ответа с кодом 200
     */
    @PutMapping
    public ResponseEntity<DocumentResponse> update(@PathVariable(name = "spaceId") int spaceId,
                                                   @PathVariable(name = "pageId") int pageId,
                                                   @RequestBody @Valid DocumentRequest documentRequest) {
        Document documentToUpdateWith = modelMapper.map(documentRequest, Document.class);

        Document updatedDocument = documentService.update(pageId, spaceId, documentToUpdateWith);

        DocumentResponse response = modelMapper.map(updatedDocument, DocumentResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Метод, отвечающий за удаление документа с заданым ID данной страницы и пространтсва
     * @param spaceId ID пространтсва
     * @param pageId ID страницы
     * @return пустой ответ с кодом 204
     */
    @DeleteMapping
    public ResponseEntity<HttpStatus> delete(@PathVariable(name = "spaceId") int spaceId,
                                             @PathVariable(name = "pageId") int pageId) {
        documentService.delete(pageId, spaceId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Метод, обрабатывающий запросы на скачку документа в PDF формате
     * @param spaceId ID пространтсва
     * @param pageId ID страницы
     * @return HTTP ответ с PDF файлом и статусом 200
     */
    @GetMapping("/pdf")
    public ResponseEntity<InputStreamResource>
    convertToPdf(@PathVariable(name = "spaceId") int spaceId,
                 @PathVariable(name = "pageId") int pageId,
                 @RequestParam(name = "font", required = false, defaultValue = "times") String font,
                 @RequestParam(name = "fontSize", required = false, defaultValue = "16")
                 @Min(value = 6, message = "Мин. размер шрифта - 6")
                 @Max(value = 66, message = "Макс. размер шрифта - 66") int fontSize,
                 @RequestParam(name = "tree", required = false, defaultValue = "false") boolean tree) {
        ConvertedDocument convertedDocument = documentService
                .convertToPdf(spaceId, pageId, font, fontSize, tree);

        InputStreamResource inputStreamResource = convertedDocument.getInputStreamResource();
        String documentName = convertedDocument.getDocumentName();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=\"%s.%s\"", documentName, "pdf"))
                .body(inputStreamResource);
    }
}