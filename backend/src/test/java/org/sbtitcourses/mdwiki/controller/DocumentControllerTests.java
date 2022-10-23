package org.sbtitcourses.mdwiki.controller;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.document.DocumentResponse;
import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.service.DocumentService;
import org.sbtitcourses.mdwiki.service.PageService;
import org.sbtitcourses.mdwiki.service.SpaceService;
import org.sbtitcourses.mdwiki.util.exception.DocumentNotFoundException;
import org.sbtitcourses.mdwiki.util.exception.PageNotFoundException;
import org.sbtitcourses.mdwiki.util.exception.SpaceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тест REST контроллера для CRUD операций над сущностью Document
 */
@WebMvcTest(DocumentController.class)
@AutoConfigureMockMvc(addFilters = false)
class DocumentControllerTests {

    @MockBean
    private SpaceService spaceService;
    @MockBean
    private PageService pageService;
    @MockBean
    private DocumentService documentService;
    @MockBean
    private ModelMapper modelMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void verifyHttpRequestMappingAndDeserialization() throws Exception {
        Space space = new Space();
        Page page = new Page();
        Document documentToCreate = new Document();
        Document createdDocument = new Document();

        when(modelMapper.map(any(), eq(Document.class))).thenReturn(documentToCreate);
        when(spaceService.get(1)).thenReturn(space);
        when(pageService.get(1, space)).thenReturn(page);
        when(documentService.create(documentToCreate)).thenReturn(createdDocument);

        mockMvc.perform(post("/spaces/{spaceId}/pages/{pageId}/document", 1, 1)
                .contentType(APPLICATION_JSON)
                .content("{\"text\": \"testText\"}"))
                .andExpect(status().isCreated());

        verify(modelMapper).map(any(), eq(Document.class));
        verify(spaceService).get(1);
        verify(pageService).get(1, space);
        verify(documentService).create(documentToCreate);
    }

    @Test
    public void verifyFieldValidation() throws Exception {
        mockMvc.perform(post("/spaces/{id}/pages/{1}/document", 1, 1)
                .contentType(APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifyResultSerialization() throws Exception {
        Space space = new Space();
        Page page = new Page();
        Document document = new Document();
        DocumentResponse documentResponse = new DocumentResponse();
        documentResponse.setText("testText");

        when(spaceService.get(1)).thenReturn(space);
        when(pageService.get(1, space)).thenReturn(page);
        when(documentService.get(page)).thenReturn(document);
        when(modelMapper.map(document, DocumentResponse.class)).thenReturn(documentResponse);

        mockMvc.perform(get("/spaces/{spaceId}/pages/{pageId}/document", 1, 1))
                .andExpect(jsonPath("$.text").value("testText"));

        verify(spaceService).get(1);
        verify(pageService).get(1, space);
        verify(documentService).get(page);
        verify(modelMapper).map(document, DocumentResponse.class);
    }

    @Test
    public void verifyErrorHandling() throws Exception {
        Space space = new Space();
        Page page = new Page();

        when(spaceService.get(1)).thenReturn(space);
        when(pageService.get(1, space)).thenReturn(page);
        when(documentService.get(page)).thenThrow(DocumentNotFoundException.class);
        when(spaceService.get(2)).thenThrow(SpaceNotFoundException.class);
        when(pageService.get(2, space)).thenThrow(PageNotFoundException.class);

        mockMvc.perform(get("/spaces/{spaceId}/pages/{pageId}/document", 1, 1))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/spaces/{spaceId}/pages/{pageId}/document", 1, 2))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/spaces/{spaceId}/pages/{pageId}/document", 2, 1))
                .andExpect(status().isNotFound());


        verify(spaceService, times(2)).get(1);
        verify(pageService).get(1, space);
        verify(documentService).get(page);
        verify(spaceService).get(2);
        verify(pageService).get(2, space);
    }
}