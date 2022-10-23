package org.sbtitcourses.mdwiki.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.PersonRepository;
import org.sbtitcourses.mdwiki.util.exception.PersonNotFoundException;

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
    @InjectMocks
    private PersonService personService;
    private final Person personToCreate;
    private final Person personWithId;
    private final Person personToUpdateWith;

    {
        personToCreate = new Person();

        personWithId = new Person();
        personWithId.setId(1);

        personToUpdateWith = new Person();
        personToUpdateWith.setId(1);
        personToUpdateWith.setUsername("newUsername");
        personToUpdateWith.setName("newName");
        personToUpdateWith.setEmail("newEmail@mail.com");
        personToUpdateWith.setEnabled(false);
    }


    @Test
    public void createShouldReturnPerson() {
        when(personRepository.save(personToCreate)).thenReturn(personWithId);

        Person createdPerson = personService.create(personToCreate);

        assertEquals(1, createdPerson.getId());
        assertTrue(createdPerson.getEnabled());
        verify(personRepository).save(personToCreate);
    }

    @Test
    public void getAllShouldReturnPersonList() {
        List<Person> people = new LinkedList<>();
        people.add(personWithId);

        when(personRepository.findAll()).thenReturn(people);

        List<Person> gottenPeople = personService.getAll();

        assertEquals(people.size(), gottenPeople.size());
        assertEquals(personWithId.getId(), gottenPeople.get(0).getId());
        verify(personRepository).findAll();
    }

    @Test
    public void getShouldReturnPerson() {
        when(personRepository.findById(1)).thenReturn(Optional.of(personWithId));
        when(personRepository.findById(2)).thenReturn(Optional.empty());

        Person gottenPerson = personService.get(1);

        assertEquals(1, gottenPerson.getId());
        assertThrows(PersonNotFoundException.class, () -> personService.get(2));
        verify(personRepository).findById(1);
        verify(personRepository).findById(2);
    }

    @Test
    public void updateShouldReturnPerson() {
        when(personRepository.findById(1)).thenReturn(Optional.of(personWithId));
        when(personRepository.findById(2)).thenReturn(Optional.empty());

        Person updatedPerson = assertDoesNotThrow(() -> personService.update(1, personToUpdateWith));

        assertEquals(1, updatedPerson.getId());
        assertEquals(personToUpdateWith.getUsername(), updatedPerson.getUsername());
        assertEquals(personToUpdateWith.getName(), updatedPerson.getName());
        assertEquals(personToUpdateWith.getEmail(), updatedPerson.getEmail());
        assertEquals(personToUpdateWith.getEnabled(), updatedPerson.getEnabled());
        assertThrows(PersonNotFoundException.class, () -> personService.update(2, personToUpdateWith));
        verify(personRepository).findById(1);
        verify(personRepository).save(personWithId);
        verify(personRepository).findById(2);
    }

    @Test
    public void deleteShouldRemovePerson() {
        when(personRepository.findById(1)).thenReturn(Optional.of(personWithId)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> personService.delete(1));
        assertThrows(PersonNotFoundException.class, () -> personService.delete(1));
        verify(personRepository, times(2)).findById(1);
        verify(personRepository).delete(personWithId);
    }
}