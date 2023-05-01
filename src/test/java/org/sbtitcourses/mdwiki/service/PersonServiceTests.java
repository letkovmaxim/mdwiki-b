package org.sbtitcourses.mdwiki.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.PersonRepository;
import org.sbtitcourses.mdwiki.util.EntityFetcher;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тест для сервиса с логикой CRUD операций над сущностью Person
 */
@ExtendWith(MockitoExtension.class)
class PersonServiceTests {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private EntityFetcher entityFetcher;
    @InjectMocks
    private PersonService personService;
    private final Person personWithId = Person.builder()
            .id(1)
            .email("testEmail@mail.com")
            .build();
    private final Person personToUpdateWith = Person.builder()
            .id(1)
            .username("newUsername")
            .name("newName")
            .email("newEmail@mail.com")
            .enabled(false)
            .build();

    @Test
    public void getAllShouldReturnPersonList() {
        List<Person> people = new LinkedList<>();
        people.add(personWithId);
        Page<Person> page = new PageImpl<>(people);

        when(personRepository.findAll(any(Pageable.class))).thenReturn(page);

        List<Person> gottenPeople = personService.get(0, 1);

        assertEquals(people.size(), gottenPeople.size());
        assertEquals(personWithId.getId(), gottenPeople.get(0).getId());
        verify(personRepository).findAll(any(Pageable.class));
    }

    @Test
    public void getShouldReturnPerson() {
        when(entityFetcher.getLoggedInUser()).thenReturn(personWithId);
        when(personRepository.findById(1)).thenReturn(Optional.of(personWithId));

        Person gottenPerson = personService.get(1);

        assertEquals(1, gottenPerson.getId());
        assertThrows(AccessDeniedException.class, () -> personService.get(2));
        verify(personRepository).findById(1);
    }

    @Test
    public void updateShouldReturnPerson() {
        when(entityFetcher.getLoggedInUser()).thenReturn(personWithId);
        when(personRepository.findById(1)).thenReturn(Optional.of(personWithId));

        Person updatedPerson = assertDoesNotThrow(() -> personService.update(1, personToUpdateWith));

        assertEquals(1, updatedPerson.getId());
        assertEquals(personToUpdateWith.getUsername(), updatedPerson.getUsername());
        assertEquals(personToUpdateWith.getName(), updatedPerson.getName());
        assertEquals(personToUpdateWith.getEmail(), updatedPerson.getEmail());
        assertEquals(personToUpdateWith.isEnabled(), updatedPerson.isEnabled());
        assertThrows(AccessDeniedException.class, () -> personService.update(2, personToUpdateWith));
        verify(personRepository).findById(1);
        verify(personRepository).save(personWithId);
    }

    @Test
    public void deleteShouldRemovePerson() {
        when(entityFetcher.getLoggedInUser()).thenReturn(personWithId);
        when(personRepository.findById(1)).thenReturn(Optional.of(personWithId)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> personService.delete(1));
        assertThrows(ElementNotFoundException.class, () -> personService.delete(1));
        verify(personRepository, times(2)).findById(1);
        verify(personRepository).delete(personWithId);
    }
}