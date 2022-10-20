package org.sbtitcourses.mdwiki.controller;

import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.page.PageRequest;
import org.sbtitcourses.mdwiki.dto.page.PageResponse;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.service.PageService;
import org.sbtitcourses.mdwiki.service.SpaceService;
import org.sbtitcourses.mdwiki.util.ErrorResponse;
import org.sbtitcourses.mdwiki.util.exception.NotFoundException;
import org.sbtitcourses.mdwiki.util.exception.PageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * REST контроллер для CRUD операций над сущностью Page
 */
@RestController
@RequestMapping("/spaces/{spaceId}")
public class PageController {

    /**
     * Сервис с логикой CRUD операций над сущностью Space
     */
    private final SpaceService spaceService;
    /**
     * Сервис с логикой CRUD операций над сущностью Page
     */
    private final PageService pageService;
    /**
     * Маппер для конвертации сущностей
     */
    private final ModelMapper modelMapper;

    /**
     * Конструктор для автоматичекого внедрения зависимостей
     * @param spaceService сервис с логикой CRUD операций над сущностью Space
     * @param pageService сервис с логикой CRUD операций над сущностью Page
     * @param modelMapper маппер для конвертации сущностей
     */
    @Autowired
    public PageController(SpaceService spaceService, PageService pageService, ModelMapper modelMapper) {
        this.spaceService = spaceService;
        this.pageService = pageService;
        this.modelMapper = modelMapper;
    }

    /**
     * Метод, отвечающий за создание новой страницы
     * @param spaceId ID пространства, в котором нужно создать страницу
     * @param pageRequest DTO сущности Page для запроса
     * @return DTO сущности Page для ответа с кодом 201
     */
    @PostMapping("/pages")
    private ResponseEntity<PageResponse> create(@PathVariable(name = "spaceId") int spaceId,
                                    @RequestBody PageRequest pageRequest) {
        Page pageToCreate = modelMapper.map(pageRequest, Page.class);
        Space space = spaceService.get(spaceId);
        pageToCreate.setSpace(space);

        Page createdPage = pageService.create(pageToCreate);

        PageResponse response = modelMapper.map(createdPage, PageResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Метод, отвечающий за создание новой подстраницы
     * @param spaceId ID пространства, в котором нужно создать подстраницу
     * @param pageId ID страницы-родителя
     * @param pageRequest DTO сущности Page для запроса
     * @return DTO сущности Page для ответа с кодом 201
     */
    @PostMapping("/pages/{pageId}")
    public ResponseEntity<PageResponse> createSubpage(@PathVariable(name = "spaceId") int spaceId,
                                      @PathVariable(name = "pageId") int pageId,
                                      @RequestBody PageRequest pageRequest) {
        Page subpageToCreate = modelMapper.map(pageRequest, Page.class);
        Space space = spaceService.get(spaceId);
        Page parent = pageService.get(pageId, space);
        subpageToCreate.setSpace(space);
        subpageToCreate.setParent(parent);

        Page createdPage = pageService.create(subpageToCreate);

        PageResponse response = modelMapper.map(createdPage, PageResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Метод, отвечающий за получение всех страниц данного пространства
     * @param spaceId ID пространства
     * @return список DTO сущности Page для ответа с кодом 200
     */
    @GetMapping("/pages")
    public ResponseEntity<List<PageResponse>> getAll(@PathVariable(name = "spaceId") int spaceId) {
        List<PageResponse> pages = new LinkedList<>();
        Space space = spaceService.get(spaceId);

        for (Page page: pageService.getAll(space)) {
            PageResponse pageResponse = modelMapper.map(page, PageResponse.class);
            pages.add(pageResponse);
        }

        return new ResponseEntity<>(pages, HttpStatus.OK);
    }

    /**
     * Метод, отвечающий за получение страницы по его ID для данного пространства
     * @param spaceId ID пространства
     * @param pageId ID страницы
     * @return DTO сущности Page для ответа с кодом 200
     */
    @GetMapping("/pages/{pageId}")
    public ResponseEntity<PageResponse> get(@PathVariable(name = "spaceId") int spaceId,
                                @PathVariable(name = "pageId") int pageId) {
        Space space = spaceService.get(spaceId);

        Page page = pageService.get(pageId, space);

        PageResponse response = modelMapper.map(page, PageResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Метод, отвечающий за обновление страницы с заданым ID для данного пространства
     * @param spaceId ID пространства
     * @param pageId ID страницы
     * @param pageRequest DTO сущности Page для запроса
     * @return DTO сущности Page для ответа с кодом 200
     */
    @PutMapping("/pages/{pageId}")
    private ResponseEntity<PageResponse> update(@PathVariable(name = "spaceId") int spaceId,
                                    @PathVariable(name = "pageId") int pageId,
                                    @RequestBody PageRequest pageRequest ) {
        Page pageToUpdateWith = modelMapper.map(pageRequest, Page.class);
        Space space = spaceService.get(spaceId);

        Page updatedPage = pageService.update(pageId, space, pageToUpdateWith);

        PageResponse response = modelMapper.map(updatedPage, PageResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Метод, отвечающий за удаление страницы с заданым ID данного пространства
     * @param spaceId ID пространства
     * @param pageId ID страницы
     * @return пустой ответ с кодом 205
     */
    @DeleteMapping("/pages/{pageId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable(name = "spaceId") int spaceId,
                           @PathVariable(name = "pageId") int pageId) {
        Space space = spaceService.get(spaceId);
        Page pageToDelete = pageService.get(pageId, space);

        pageService.delete(pageToDelete.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}