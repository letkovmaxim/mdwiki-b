package org.sbtitcourses.mdwiki.controller;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.person.PersonResponse;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.service.PersonService;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
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
 * Тест REST контроллера для CRUD операций над сущностью Person
 */
@WebMvcTest(PersonController.class)
@AutoConfigureMockMvc(addFilters = false)
class PersonControllerTests {

    @MockBean
    private PersonService personService;
    @MockBean
    private ModelMapper modelMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void verifyResultSerialization() throws Exception {
        Person person = new Person();
        PersonResponse personResponse = new PersonResponse();
        personResponse.setUsername("testUsername");
        personResponse.setName("testName");
        personResponse.setEmail("testEmail@test.test");
        personResponse.setCreatedAt(Instant.now());
        personResponse.setUpdatedAt(Instant.now());
        personResponse.setEnabled(true);

        when(personService.get(1)).thenReturn(person);
        when(modelMapper.map(person, PersonResponse.class)).thenReturn(personResponse);

        mockMvc.perform(get("/people/{id}", 1))
                .andExpect(jsonPath("$.username").value("testUsername"))
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.email").value("testEmail@test.test"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.enabled").value(true));

        verify(personService).get(1);
        verify(modelMapper).map(person, PersonResponse.class);
    }

    @Test
    public void verifyErrorHandling() throws Exception {
        when(personService.get(1)).thenThrow(new ElementNotFoundException("Not Found"));

        mockMvc.perform(get("/people/{id}", 1))
                .andExpect(status().isNotFound());

        verify(personService).get(1);
    }
}