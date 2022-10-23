package org.sbtitcourses.mdwiki.controller;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.page.PageResponse;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.service.PageService;
import org.sbtitcourses.mdwiki.service.SpaceService;
import org.sbtitcourses.mdwiki.util.exception.PageNotFoundException;
import org.sbtitcourses.mdwiki.util.exception.SpaceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тест REST контроллера для CRUD операций над сущностью Page
 */
@WebMvcTest(PageController.class)
@AutoConfigureMockMvc(addFilters = false)
class PageControllerTests {

    @MockBean
    private SpaceService spaceService;
    @MockBean
    private PageService pageService;
    @MockBean
    private ModelMapper modelMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void verifyHttpRequestMappingAndDeserialization() throws Exception {
        Page pageToCreate = new Page();
        Space space = new Space();
        Page createdPage = new Page();

        when(modelMapper.map(any(), eq(Page.class))).thenReturn(pageToCreate);
        when(spaceService.get(1)).thenReturn(space);
        when(pageService.create(pageToCreate)).thenReturn(createdPage);

        mockMvc.perform(post("/spaces/{spaceId}/pages", 1)
                .contentType(APPLICATION_JSON)
                .content("{\"name\": \"testName\", \"public\": \"false\"}"))
                .andExpect(status().isCreated());

        verify(modelMapper).map(any(), eq(Page.class));
        verify(spaceService).get(1);
        verify(pageService).create(pageToCreate);
    }

    @Test
    public void verifyFieldValidation() throws Exception {
        mockMvc.perform(post("/spaces/{spaceId}/pages", 1)
                .contentType(APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifyResultSerialization() throws Exception {
        Space space = new Space();
        Page page = new Page();
        PageResponse pageResponse = new PageResponse();
        pageResponse.setName("testName");
        pageResponse.setCreatedAt(new Date());
        pageResponse.setUpdatedAt(new Date());
        pageResponse.setPublic(false);

        when(spaceService.get(1)).thenReturn(space);
        when(pageService.get(1, space)).thenReturn(page);
        when(modelMapper.map(page, PageResponse.class)).thenReturn(pageResponse);

        mockMvc.perform(get("/spaces/{spaceId}/pages/{pageId}", 1, 1))
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.public").value(false));

        verify(spaceService).get(1);
        verify(pageService).get(1, space);
        verify(modelMapper).map(page, PageResponse.class);
    }

    @Test
    public void verifyErrorHandling() throws Exception {
        Space space = new Space();

        when(spaceService.get(1)).thenReturn(space);
        when(pageService.get(1, space)).thenThrow(PageNotFoundException.class);
        when(spaceService.get(2)).thenThrow(SpaceNotFoundException.class);

        mockMvc.perform(get("/spaces/{spaceId}/pages/{pageId}", 1, 1))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/spaces/{spaceId}/pages/{pageId}", 2, 1))
                .andExpect(status().isNotFound());

        verify(spaceService).get(1);
        verify(pageService).get(1, space);
        verify(spaceService).get(2);
    }
}