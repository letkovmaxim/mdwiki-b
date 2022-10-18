package org.sbtitcourses.mdwiki.controller;

import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.PageRequest;
import org.sbtitcourses.mdwiki.dto.PageResponse;
import org.sbtitcourses.mdwiki.dto.SpaceRequest;
import org.sbtitcourses.mdwiki.dto.SpaceResponse;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.service.PageService;
import org.sbtitcourses.mdwiki.service.SpaceService;
import org.sbtitcourses.mdwiki.util.ErrorResponse;
import org.sbtitcourses.mdwiki.util.exception.PageNotFoundException;
import org.sbtitcourses.mdwiki.util.exception.SpaceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
public class WorkspaceController {

    private final SpaceService spaceService;
    private final PageService pageService;
    private final ModelMapper modelMapper;

    @Autowired
    public WorkspaceController(SpaceService spaceService, PageService pageService, ModelMapper modelMapper) {
        this.spaceService = spaceService;
        this.pageService = pageService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/spaces")
    public SpaceResponse createSpace(@RequestBody SpaceRequest spaceRequest) {
        Space spaceToCreate = modelMapper.map(spaceRequest, Space.class);

        return modelMapper.map(spaceService.create(spaceToCreate), SpaceResponse.class);
    }

    @GetMapping("/spaces")
    public List<SpaceResponse> getAllSpaces() {
        List<SpaceResponse> spaces = new LinkedList<>();
        for (Space space: spaceService.getAll()) {
            spaces.add(modelMapper.map(space, SpaceResponse.class));
        }

        return spaces;
    }

    @GetMapping("/spaces/{id}")
    public SpaceResponse getSpace(@PathVariable(name = "id") int id) {
        Space space = spaceService.get(id);

        return modelMapper.map(space, SpaceResponse.class);
    }

    @PutMapping("/spaces/{id}")
    public SpaceResponse updateSpace(@PathVariable(name = "id") int id,
                                     @RequestBody SpaceRequest spaceRequest) {
        Space spaceToUpdateWith = modelMapper.map(spaceRequest, Space.class);

        return modelMapper.map(spaceService.update(id, spaceToUpdateWith), SpaceResponse.class);
    }

    @DeleteMapping("/spaces/{id}")
    public void delete(@PathVariable int id) {
        spaceService.delete(id);
    }

    @PostMapping("/spaces/{id}/pages")
    private PageResponse createPage(@PathVariable(name = "id") int id,
                                    @RequestBody PageRequest pageRequest) {
        Page pageToCreate = modelMapper.map(pageRequest, Page.class);
        pageToCreate.setSpace(spaceService.get(id));

        return modelMapper.map(pageService.create(pageToCreate), PageResponse.class);
    }

    @PostMapping("/spaces/{spaceId}/pages/{pageId}")
    public PageResponse createSubpage(@PathVariable(name = "spaceId") int spaceId,
                              @PathVariable(name = "pageId") int pageId,
                              @RequestBody PageRequest pageRequest) {
        Page subpageToCreate = modelMapper.map(pageRequest, Page.class);
        Space space = spaceService.get(spaceId);
        subpageToCreate.setSpace(space);
        subpageToCreate.setParent(pageService.get(pageId, space));

        return modelMapper.map(pageService.create(subpageToCreate), PageResponse.class);
    }

    @GetMapping("/spaces/{id}/pages")
    public List<PageResponse> getAllPages(@PathVariable(name = "id") int spaceId) {
        List<PageResponse> pages = new LinkedList<>();
        for (Page page: pageService.getAll(spaceService.get(spaceId))) {
            pages.add(modelMapper.map(page, PageResponse.class));
        }

        return pages;
    }

    @GetMapping("/spaces/{spaceId}/pages/{pageId}")
    public PageResponse getPage(@PathVariable(name = "spaceId") int spaceId,
                                @PathVariable(name = "pageId") int pageId) {
        Page page = pageService.get(pageId, spaceService.get(spaceId));

        return modelMapper.map(page, PageResponse.class);
    }

    @PutMapping("/spaces/{spaceId}/pages/{pageId}")
    private PageResponse updatePage(@PathVariable(name = "spaceId") int spaceId,
                                    @PathVariable(name = "pageId") int pageId,
                                    @RequestBody PageRequest pageRequest ) {
        Page pageToUpdate = pageService.get(pageId, spaceService.get(spaceId));
        Page pageToUpdateWith = modelMapper.map(pageRequest, Page.class);

        return modelMapper.map(pageService.update(pageToUpdate.getId(), pageToUpdateWith), PageResponse.class);
    }

    @DeleteMapping("/spaces/{spaceId}/pages/{pageId}")
    public void delete(@PathVariable(name = "spaceId") int spaceId,
                       @PathVariable(name = "pageId") int pageId) {
        Page pageToDelete = pageService.get(pageId, spaceService.get(spaceId));
        pageService.delete(pageToDelete.getId());
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleSpaceNotFoundException(SpaceNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Пространство не найдено",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlePageNotFoundException(PageNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Страница не найдена",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    //TODO: ADD VALIDATION ERROR HANDLER
}
