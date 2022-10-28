package org.sbtitcourses.mdwiki.controller;

import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.document.DocumentRequest;
import org.sbtitcourses.mdwiki.dto.document.DocumentResponse;
import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.service.DocumentService;
import org.sbtitcourses.mdwiki.service.PageService;
import org.sbtitcourses.mdwiki.service.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST контроллер для CRUD операций над сущностью Document
 */
@RestController
@RequestMapping("/spaces/{spaceId}/pages/{pageId}/document")
public class DocumentController {

    /**
     * Сервис с логикой CRUD операций над сущностью Space
     */
    private final SpaceService spaceService;
    /**
     * Сервис с логикой CRUD операций над сущностью Page
     */
    private final PageService pageService;
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
     * @param spaceService сервис с логикой CRUD операций над сущностью Space
     * @param pageService сервис с логикой CRUD операций над сущностью Page
     * @param documentService сервис с логикой CRUD операций над сущностью Document
     * @param modelMapper маппер для конвертации сущностей
     */
    @Autowired
    public DocumentController(SpaceService spaceService, PageService pageService, DocumentService documentService, ModelMapper modelMapper) {
        this.spaceService = spaceService;
        this.pageService = pageService;
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
        Document documentToCreate = modelMapper.map(documentRequest, Document.class);
        Space space = spaceService.get(spaceId);
        Page page = pageService.get(pageId, space);
        documentToCreate.setPage(page);

        Document createdDocument = documentService.create(documentToCreate);

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
        Space space = spaceService.get(spaceId);
        Page page = pageService.get(pageId, space);

        Document document = documentService.get(page);

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
        Space space = spaceService.get(spaceId);
        Page page = pageService.get(pageId, space);

        Document updatedDocument = documentService.update(page, documentToUpdateWith);

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
        Space space = spaceService.get(spaceId);
        Page page = pageService.get(pageId, space);

        documentService.delete(page);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}