package org.sbtitcourses.mdwiki.controller;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.document.DocumentResponse;
import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.service.DocumentService;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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
 * Тест REST контроллера для CRUD операций над сущностью Document
 */
@WebMvcTest(DocumentController.class)
@AutoConfigureMockMvc(addFilters = false)
class DocumentControllerTests {

    @MockBean
    private DocumentService documentService;
    @MockBean
    private ModelMapper modelMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void verifyHttpRequestMappingAndDeserialization() throws Exception {
        Document documentToCreate = new Document();
        Document createdDocument = new Document();

        when(modelMapper.map(any(), eq(Document.class))).thenReturn(documentToCreate);
        when(documentService.create(documentToCreate, 1, 1)).thenReturn(createdDocument);

        mockMvc.perform(post("/spaces/{spaceId}/pages/{pageId}/document", 1, 1)
                .contentType(APPLICATION_JSON)
                .content("{\"text\": \"testText\"}"))
                .andExpect(status().isCreated());

        verify(modelMapper).map(any(), eq(Document.class));
        verify(documentService).create(documentToCreate, 1, 1);
    }

    @Test
    public void verifyFieldValidation() throws Exception {
        mockMvc.perform(post("/spaces/{spaceId}/pages/{pageId}/document", 1, 1)
                .contentType(APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifyResultSerialization() throws Exception {
        Document document = new Document();
        DocumentResponse documentResponse = new DocumentResponse();
        documentResponse.setText("testText");

        when(documentService.get(1, 1)).thenReturn(document);
        when(modelMapper.map(document, DocumentResponse.class)).thenReturn(documentResponse);

        mockMvc.perform(get("/spaces/{spaceId}/pages/{pageId}/document", 1, 1))
                .andExpect(jsonPath("$.text").value("testText"));

        verify(documentService).get(1, 1);
        verify(modelMapper).map(document, DocumentResponse.class);
    }

    @Test
    public void verifyErrorHandling() throws Exception {
        when(documentService.get(1, 1)).thenThrow(new ElementNotFoundException("Страница не найдена"));
        when(documentService.get(1, 2)).thenThrow(new ElementNotFoundException("Пространство не найдено"));
        when(documentService.get(2, 1)).thenThrow(new ElementNotFoundException("Документ не найден"));
        when(documentService.get(2, 2)).thenThrow(new AccessDeniedException("Доступ запрещен"));

        mockMvc.perform(get("/spaces/{spaceId}/pages/{pageId}/document", 1, 1))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/spaces/{spaceId}/pages/{pageId}/document", 1, 2))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/spaces/{spaceId}/pages/{pageId}/document", 2, 1))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/spaces/{spaceId}/pages/{pageId}/document", 2, 2))
                .andExpect(status().isForbidden());

        verify(documentService).get(1, 1);
        verify(documentService).get(1, 2);
        verify(documentService).get(2, 2);
        verify(documentService).get(2, 2);
    }
}