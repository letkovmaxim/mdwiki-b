package org.sbtitcourses.mdwiki.controller;

import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.page.SearchPageResponse;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;


/**
 * Котроллер для поиска страниц.
 */
@RestController
@RequestMapping("/pages")
@CrossOrigin
public class SearchController {

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
    public SearchController(PageService pageService, ModelMapper modelMapper) {
        this.pageService = pageService;
        this.modelMapper = modelMapper;
    }

    /**
     * Метод, обрабатывающий запрос на получение всех страниц по подстроке имени.
     *
     * @param searchPage подстрака имени страницы.
     * @return HTTP ответ со списком страниц и статусом 200.
     */
    @GetMapping("/{searchPage}")
    public ResponseEntity<List<SearchPageResponse>> search(@PathVariable("searchPage") String searchPage){

        List<SearchPageResponse> pages = new LinkedList<>();

        for (Page page : pageService.get(searchPage)) {
            SearchPageResponse pageResponse = modelMapper.map(page, SearchPageResponse.class);
            pageResponse.setSpaceId(page.getSpace().getId());
            pageResponse.setSpaceName(page.getSpace().getName());
            pages.add(pageResponse);
        }

        return ResponseEntity.ok().body(pages);
    }
}
