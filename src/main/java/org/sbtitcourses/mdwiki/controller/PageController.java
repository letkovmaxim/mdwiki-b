package org.sbtitcourses.mdwiki.controller;

import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.page.PageRequest;
import org.sbtitcourses.mdwiki.dto.page.PageResponse;
import org.sbtitcourses.mdwiki.dto.page.PlainPageResponse;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.LinkedList;
import java.util.List;

/**
 * REST контроллер, обрабатывающий запросы на взаимодействие с сущностью {@link Page}.
 */
@RestController
@RequestMapping("/spaces/{spaceId}/pages")
@Validated
public class PageController {

    /**
     * Сервис с логикой взаимодействия с сущностью {@link Page}.
     */
    private final PageService pageService;

    /**
     * Маппер для конвертации сущностей.
     */
    private final ModelMapper modelMapper;

    /**
     * Конструктор для автоматичекого внедрения зависимостей.
     *
     * @param pageService сервис с логикой взаимодействия с сущностью {@link Page}.
     * @param modelMapper маппер для конвертации сущностей.
     */
    @Autowired
    public PageController(PageService pageService, ModelMapper modelMapper) {
        this.pageService = pageService;
        this.modelMapper = modelMapper;
    }

    /**
     * Метод, обрабатывающий запрос на создание новой страницы.
     *
     * @param spaceId     ID пространства, в котором нужно создать страницу.
     * @param pageRequest информация о новой странице.
     * @return HTTP ответ с информацией о новой станице и статусом 201.
     */
    @PostMapping
    public ResponseEntity<PageResponse> create(@PathVariable("spaceId") int spaceId,
                                                @RequestBody @Valid PageRequest pageRequest) {
        Page page = modelMapper.map(pageRequest, Page.class);

        Page createdPage = pageService.create(page, spaceId);

        PageResponse response = modelMapper.map(createdPage, PageResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Метод, обрабатывающий запрос на создание новой подстраницы.
     *
     * @param spaceId     ID пространства, в котором нужно создать подстраницу.
     * @param parentId    ID страницы-родителя.
     * @param pageRequest информация о новой подстранице.
     * @return HTTP ответ с информацией о новой подстанице и статусом 201.
     */
    @PostMapping("/{parentId}")
    public ResponseEntity<PageResponse> createSubpage(@PathVariable("spaceId") int spaceId,
                                                      @PathVariable("parentId") int parentId,
                                                      @RequestBody @Valid PageRequest pageRequest) {
        Page subpage = modelMapper.map(pageRequest, Page.class);

        Page createdSubpage = pageService.createSubpage(subpage, parentId, spaceId);

        PageResponse response = modelMapper.map(createdSubpage, PageResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Метод, обрабатывающий запрос на получение всех страниц данного пространства.
     *
     * @param spaceId ID пространства.
     * @param bunch   номер страницы при пагинации.
     * @param size    количество элементов в странице при пагинации.
     * @return HTTP ответ со списком страниц и статусом 200.
     */
    @GetMapping
    public ResponseEntity<List<PageResponse>> get(@PathVariable("spaceId") int spaceId,
                                                  @RequestParam("bunch") @Min(0) int bunch,
                                                  @RequestParam("size") @Min(1) int size) {
        List<PageResponse> pages = new LinkedList<>();

        for (Page page : pageService.get(spaceId, bunch, size)) {
            PageResponse pageResponse = modelMapper.map(page, PageResponse.class);
            pages.add(pageResponse);
        }

        return ResponseEntity.ok().body(pages);
    }

    /**
     * Метод, обрабатывающий запрос на получение всех страниц данного пространства без списка подстраниц.
     *
     * @param spaceId ID пространства.
     * @param bunch   номер страницы при пагинации.
     * @param size    количество элементов в странице при пагинации.
     * @return HTTP ответ со списком страниц и статусом 200.
     */
    @GetMapping("/plain")
    public ResponseEntity<List<PlainPageResponse>> getPlain(@PathVariable("spaceId") int spaceId,
                                                            @RequestParam("bunch") @Min(0) int bunch,
                                                            @RequestParam("size") @Min(1) int size) {
        List<PlainPageResponse> pages = new LinkedList<>();

        for (Page page : pageService.get(spaceId, bunch, size)) {
            PlainPageResponse plainPageResponse = modelMapper.map(page, PlainPageResponse.class);
            pages.add(plainPageResponse);
        }

        return ResponseEntity.ok().body(pages);
    }

    /**
     * Метод, обрабатывающий запрос на получение родителя страницы.
     *
     * @param spaceId ID пространства.
     * @param pageId  ID страницы, родителя которой нужно получить.
     * @return HTTP ответ с информацией о странице и статусом 200.
     */
    @GetMapping("/{pageId}/parent")
    public ResponseEntity<PageResponse> getParent(@PathVariable("spaceId") int spaceId,
                                                  @PathVariable("pageId") int pageId) {
        Page parent = pageService.getParent(pageId, spaceId);

        PageResponse response = modelMapper.map(parent, PageResponse.class);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Метод, обрабатывающий запрос на получение страницы по его ID для данного пространства.
     *
     * @param spaceId ID пространства.
     * @param pageId  ID страницы.
     * @return HTTP ответ с информацией о странице и статусом 200.
     */
    @GetMapping("/{pageId}")
    public ResponseEntity<PageResponse> get(@PathVariable("spaceId") int spaceId,
                                            @PathVariable("pageId") int pageId) {
        Page page = pageService.get(pageId, spaceId);

        PageResponse response = modelMapper.map(page, PageResponse.class);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Метод, обрабатывающий запрос на получение подстраниц по ID страницы-родителя.
     *
     * @param spaceId ID пространства.
     * @param pageId  ID страницы-родителя.
     * @return HTTP ответ со списком подстраниц и статусом 200.
     */
    @GetMapping("/{pageId}/subpages")
    public ResponseEntity<List<PlainPageResponse>> getSubpages(@PathVariable("spaceId") int spaceId,
                                                               @PathVariable("pageId") int pageId) {
        Page page = pageService.get(pageId, spaceId);
        List<PlainPageResponse> subpages = new LinkedList<>();

        for (Page subpage : page.getSubpages()) {
            PlainPageResponse plainPageResponse = modelMapper.map(subpage, PlainPageResponse.class);
            subpages.add(plainPageResponse);
        }

        return ResponseEntity.ok().body(subpages);
    }

    /**
     * Метод, обрабатывающий запрос на обновление страницы по его ID для данного пространства.
     *
     * @param spaceId     ID пространства.
     * @param pageId      ID страницы.
     * @param pageRequest информация о странице, которую нужно обновить.
     * @return HTTP ответ с информацией об обновленной странице и статусом 200.
     */
    @PutMapping("/{pageId}")
    public ResponseEntity<PageResponse> update(@PathVariable("spaceId") int spaceId,
                                                @PathVariable("pageId") int pageId,
                                                @RequestBody @Valid PageRequest pageRequest) {
        Page pageToUpdateWith = modelMapper.map(pageRequest, Page.class);

        Page updatedPage = pageService.update(pageId, spaceId, pageToUpdateWith);

        PageResponse response = modelMapper.map(updatedPage, PageResponse.class);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Метод, обрабатывающий запрос на удаление страницы по его ID для данного пространства.
     *
     * @param spaceId ID пространства.
     * @param pageId  ID страницы.
     * @return HTTP ответ со статусом 204.
     */
    @DeleteMapping("/{pageId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("spaceId") int spaceId,
                                             @PathVariable("pageId") int pageId) {
        pageService.delete(pageId, spaceId);

        return ResponseEntity.noContent().build();
    }
}