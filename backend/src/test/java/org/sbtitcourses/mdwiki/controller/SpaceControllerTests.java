package org.sbtitcourses.mdwiki.controller;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.space.SpaceResponse;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.service.SpaceService;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тест REST контроллера для CRUD операций над сущностью Space
 */

@WebMvcTest(SpaceController.class)
@AutoConfigureMockMvc(addFilters = false)
class SpaceControllerTests {

    @MockBean
    private SpaceService spaceService;
    @MockBean
    private ModelMapper modelMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void verifyHttpRequestMappingAndDeserialization() throws Exception {
        Space spaceToCreate = new Space();
        Space createdSpace = new Space();

        when(modelMapper.map(any(), eq(Space.class))).thenReturn(spaceToCreate);
        when(spaceService.create(spaceToCreate)).thenReturn(createdSpace);

        mockMvc.perform(post("/spaces")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"testName\", \"public\": false}"))
                .andExpect(status().isCreated());

        verify(modelMapper).map(any(), eq(Space.class));
        verify(spaceService).create(spaceToCreate);
    }

    @Test
    public void verifyFieldValidation() throws Exception {
        mockMvc.perform(post("/spaces")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifyResultSerialization() throws Exception {
        Space space = new Space();
        SpaceResponse spaceResponse = new SpaceResponse();
        spaceResponse.setName("testName");
        spaceResponse.setCreatedAt(new Date());
        spaceResponse.setUpdatedAt(new Date());
        spaceResponse.setShared(false);

        when(spaceService.get(1)).thenReturn(space);
        when(modelMapper.map(space, SpaceResponse.class)).thenReturn(spaceResponse);

        mockMvc.perform(get("/spaces/{id}", 1))
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.shared").value(false));

        verify(spaceService).get(1);
        verify(modelMapper).map(space, SpaceResponse.class);
    }

    @Test
    public void verifyErrorHandling() throws Exception {
        when(spaceService.get(1)).thenThrow(new ElementNotFoundException("Пространство не найдено"));
        when(spaceService.get(2)).thenThrow(new AccessDeniedException("Доступ запрещен"));

        mockMvc.perform(get("/spaces/{id}", 1))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/spaces/{id}", 2))
                .andExpect(status().isForbidden());

        verify(spaceService).get(1);
        verify(spaceService).get(2);
    }
}